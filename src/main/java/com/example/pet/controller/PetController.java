// PetController.java - 실제 Oracle DB 연동 버전
package com.example.pet.controller;

import com.example.pet.domain.Member;
import com.example.pet.domain.Pet;
import com.example.pet.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class PetController {

    private final MemberService memberService;

    @Autowired
    public PetController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 메인 페이지 (HTML + CSS + JS 포함)
    @GetMapping("/")
    @ResponseBody
    public String home() {
        return """
        <!DOCTYPE html>
        <html lang="ko">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Pet-Project</title>
            <style>
                * { margin: 0; padding: 0; box-sizing: border-box; }
                body {
                    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
                    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                    min-height: 100vh;
                    color: #333;
                }
                .container { max-width: 1200px; margin: 0 auto; padding: 20px; }
                .header {
                    background: rgba(255, 255, 255, 0.95);
                    backdrop-filter: blur(10px);
                    border-radius: 15px;
                    padding: 20px 30px;
                    margin-bottom: 30px;
                    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
                    display: flex;
                    justify-content: space-between;
                    align-items: center;
                }
                .logo { display: flex; align-items: center; gap: 15px; }
                .logo h1 { color: #4f46e5; font-size: 2rem; font-weight: 700; }
                .logo span { font-size: 2.5rem; }
                .nav-buttons { display: flex; gap: 15px; }
                .btn {
                    padding: 12px 24px; border: none; border-radius: 25px;
                    font-weight: 600; cursor: pointer; transition: all 0.3s ease;
                    text-decoration: none; display: inline-flex; align-items: center; gap: 8px;
                }
                .btn-primary { background: linear-gradient(135deg, #4f46e5 0%, #7c3aed 100%); color: white; }
                .btn-secondary { background: white; color: #4f46e5; border: 2px solid #4f46e5; }
                .btn:hover { transform: translateY(-2px); box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2); }
                .main-content { display: grid; grid-template-columns: 1fr 1fr; gap: 30px; margin-bottom: 30px; }
                .content-card {
                    background: white; border-radius: 20px; padding: 30px;
                    box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1); transition: transform 0.3s ease;
                }
                .content-card:hover { transform: translateY(-5px); }
                .card-header { display: flex; align-items: center; gap: 15px; margin-bottom: 25px; }
                .card-icon {
                    width: 60px; height: 60px; border-radius: 15px;
                    display: flex; align-items: center; justify-content: center;
                    font-size: 1.8rem; color: white;
                }
                .icon-monitoring { background: linear-gradient(135deg, #10b981 0%, #059669 100%); }
                .icon-voice { background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%); }
                .card-title { font-size: 1.5rem; font-weight: 700; color: #111827; }
                .modal {
                    position: fixed; top: 0; left: 0; width: 100%; height: 100%;
                    background: rgba(0, 0, 0, 0.5); display: none;
                    justify-content: center; align-items: center; z-index: 1000;
                }
                .modal-content {
                    background: white; border-radius: 20px; padding: 40px;
                    width: 90%; max-width: 450px; position: relative;
                }
                .close-modal {
                    position: absolute; top: 15px; right: 20px; background: none;
                    border: none; font-size: 1.5rem; cursor: pointer; color: #6b7280;
                }
                .form-group { margin-bottom: 20px; }
                .form-group label { display: block; margin-bottom: 8px; font-weight: 600; color: #374151; }
                .form-group input {
                    width: 100%; padding: 12px 16px; border: 2px solid #e5e7eb;
                    border-radius: 10px; font-size: 1rem; transition: border-color 0.3s ease;
                }
                .form-group input:focus { outline: none; border-color: #4f46e5; }
                .success-message {
                    background: #d1fae5; color: #065f46; padding: 15px;
                    border-radius: 10px; margin-bottom: 20px; text-align: center;
                }
                .error-message {
                    background: #fee2e2; color: #991b1b; padding: 15px;
                    border-radius: 10px; margin-bottom: 20px; text-align: center;
                }
                @media (max-width: 768px) {
                    .main-content { grid-template-columns: 1fr; }
                    .header { flex-direction: column; gap: 20px; }
                }
            </style>
        </head>
        <body>
            <div class="container">
                <div class="header">
                    <div class="logo">
                        <span>🐕</span>
                        <h1>Pet-Project</h1>
                    </div>
                    <div class="nav-buttons">
                        <button class="btn btn-secondary" onclick="showLogin()">로그인</button>
                        <button class="btn btn-primary" onclick="showRegister()">회원가입</button>
                    </div>
                </div>
                
                <div class="main-content">
                    <div class="content-card">
                        <div class="card-header">
                            <div class="card-icon icon-monitoring">📹</div>
                            <div>
                                <h2 class="card-title">실시간 모니터링</h2>
                                <p>언제 어디서든 반려견의 상태를 실시간으로 확인하세요</p>
                            </div>
                        </div>
                        <ul style="list-style: none; padding-left: 0">
                            <li style="margin-bottom: 10px">📍 GPS 위치 추적</li>
                            <li style="margin-bottom: 10px">📊 활동량 모니터링</li>
                            <li style="margin-bottom: 10px">🎥 라이브 영상 스트리밍</li>
                            <li style="margin-bottom: 10px">🔔 실시간 알림</li>
                        </ul>
                    </div>
                    
                    <div class="content-card">
                        <div class="card-header">
                            <div class="card-icon icon-voice">🎤</div>
                            <div>
                                <h2 class="card-title">음성 명령 관리</h2>
                                <p>주인의 목소리로 반려견을 원격 제어하세요</p>
                            </div>
                        </div>
                        <ul style="list-style: none; padding-left: 0">
                            <li style="margin-bottom: 10px">🎙️ 음성 명령 녹음</li>
                            <li style="margin-bottom: 10px">📱 원터치 명령 전송</li>
                            <li style="margin-bottom: 10px">🔊 고품질 음성 재생</li>
                            <li style="margin-bottom: 10px">📚 명령어 관리</li>
                        </ul>
                    </div>
                </div>
            </div>
            
            <!-- 로그인 모달 -->
            <div class="modal" id="loginModal">
                <div class="modal-content">
                    <button class="close-modal" onclick="closeModal('loginModal')">&times;</button>
                    <h2 style="margin-bottom: 25px; text-align: center">로그인</h2>
                    <div id="loginMessage"></div>
                    <form id="loginForm">
                        <div class="form-group">
                            <label for="loginEmail">이메일</label>
                            <input type="email" id="loginEmail" required />
                        </div>
                        <div class="form-group">
                            <label for="loginPassword">비밀번호</label>
                            <input type="password" id="loginPassword" required />
                        </div>
                        <button type="submit" class="btn btn-primary" style="width: 100%">로그인</button>
                    </form>
                </div>
            </div>
            
            <!-- 회원가입 모달 -->
            <div class="modal" id="registerModal">
                <div class="modal-content">
                    <button class="close-modal" onclick="closeModal('registerModal')">&times;</button>
                    <h2 style="margin-bottom: 25px; text-align: center">회원가입</h2>
                    <div id="registerMessage"></div>
                    <form id="registerForm">
                        <div class="form-group">
                            <label for="registerName">이름</label>
                            <input type="text" id="registerName" required />
                        </div>
                        <div class="form-group">
                            <label for="registerEmail">이메일</label>
                            <input type="email" id="registerEmail" required />
                        </div>
                        <div class="form-group">
                            <label for="registerPassword">비밀번호</label>
                            <input type="password" id="registerPassword" required />
                        </div>
                        <div class="form-group">
                            <label for="petName">반려견 이름</label>
                            <input type="text" id="petName" required />
                        </div>
                        <div class="form-group">
                            <label for="petBreed">품종</label>
                            <input type="text" id="petBreed" required />
                        </div>
                        <button type="submit" class="btn btn-primary" style="width: 100%">회원가입</button>
                    </form>
                </div>
            </div>
            
            <script>
                // 모달 관리
                function showLogin() {
                    document.getElementById("loginModal").style.display = "flex";
                }
                
                function showRegister() {
                    document.getElementById("registerModal").style.display = "flex";
                }
                
                function closeModal(modalId) {
                    document.getElementById(modalId).style.display = "none";
                    document.getElementById(modalId.replace('Modal', 'Message')).innerHTML = '';
                }
                
                // 실제 회원가입 처리
                document.getElementById('registerForm').addEventListener('submit', async function(e) {
                    e.preventDefault();
                    
                    const formData = new FormData();
                    formData.append('name', document.getElementById('registerName').value);
                    formData.append('email', document.getElementById('registerEmail').value);
                    formData.append('password', document.getElementById('registerPassword').value);
                    formData.append('petName', document.getElementById('petName').value);
                    formData.append('petBreed', document.getElementById('petBreed').value);
                    
                    try {
                        const response = await fetch('/api/register', {
                            method: 'POST',
                            body: formData
                        });
                        
                        const result = await response.text();
                        
                        if (response.ok) {
                            document.getElementById('registerMessage').innerHTML = 
                                '<div class="success-message">회원가입이 완료되었습니다!</div>';
                            document.getElementById('registerForm').reset();
                            setTimeout(() => {
                                closeModal('registerModal');
                                showLogin();
                            }, 1500);
                        } else {
                            document.getElementById('registerMessage').innerHTML = 
                                '<div class="error-message">' + result + '</div>';
                        }
                    } catch (error) {
                        document.getElementById('registerMessage').innerHTML = 
                            '<div class="error-message">오류가 발생했습니다.</div>';
                    }
                });
                
                // 실제 로그인 처리
                document.getElementById('loginForm').addEventListener('submit', async function(e) {
                    e.preventDefault();
                    
                    const formData = new FormData();
                    formData.append('email', document.getElementById('loginEmail').value);
                    formData.append('password', document.getElementById('loginPassword').value);
                    
                    try {
                        const response = await fetch('/api/login', {
                            method: 'POST',
                            body: formData
                        });
                        
                        if (response.ok) {
                            document.getElementById('loginMessage').innerHTML = 
                                '<div class="success-message">로그인 성공!</div>';
                            setTimeout(() => {
                                window.location.href = '/dashboard';
                            }, 1000);
                        } else {
                            const result = await response.text();
                            document.getElementById('loginMessage').innerHTML = 
                                '<div class="error-message">' + result + '</div>';
                        }
                    } catch (error) {
                        document.getElementById('loginMessage').innerHTML = 
                            '<div class="error-message">오류가 발생했습니다.</div>';
                    }
                });
                
                // 모달 외부 클릭 시 닫기
                window.onclick = function(event) {
                    const loginModal = document.getElementById("loginModal");
                    const registerModal = document.getElementById("registerModal");
                    
                    if (event.target == loginModal) loginModal.style.display = "none";
                    if (event.target == registerModal) registerModal.style.display = "none";
                }
            </script>
        </body>
        </html>
        """;
    }

    // 실제 회원가입 API
    @PostMapping("/api/register")
    @ResponseBody
    public String register(@RequestParam("name") String name,
                           @RequestParam("email") String email,
                           @RequestParam("password") String password,
                           @RequestParam("petName") String petName,
                           @RequestParam("petBreed") String petBreed) {
        try {
            memberService.joinWithPet(name, email, password, petName, petBreed);
            return "회원가입이 완료되었습니다!";
        } catch (IllegalStateException e) {
            return e.getMessage();
        } catch (Exception e) {
            return "회원가입 중 오류가 발생했습니다.";
        }
    }

    // 실제 로그인 API
    @PostMapping("/api/login")
    @ResponseBody
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password) {
        try {
            Optional<Member> member = memberService.loginWithPets(email, password);
            if (member.isPresent()) {
                return "로그인 성공";
            } else {
                return "이메일 또는 비밀번호가 틀렸습니다.";
            }
        } catch (Exception e) {
            return "로그인 중 오류가 발생했습니다.";
        }
    }

    // 대시보드 페이지 (로그인 후)
    @GetMapping("/dashboard")
    @ResponseBody
    public String dashboard() {
        return """
        <!DOCTYPE html>
        <html lang="ko">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Pet-Project Dashboard</title>
            <style>
                body {
                    font-family: Arial, sans-serif;
                    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                    margin: 0; padding: 20px; min-height: 100vh; color: white;
                }
                .container { max-width: 800px; margin: 0 auto; text-align: center; }
                .success-card {
                    background: white; color: #333; padding: 40px;
                    border-radius: 20px; box-shadow: 0 15px 35px rgba(0, 0, 0, 0.2);
                }
                .btn {
                    background: #4f46e5; color: white; padding: 15px 30px;
                    border: none; border-radius: 25px; margin: 10px;
                    cursor: pointer; text-decoration: none; display: inline-block;
                }
            </style>
        </head>
        <body>
            <div class="container">
                <div class="success-card">
                    <h1>🎉 환영합니다!</h1>
                    <p>Pet-Project에 성공적으로 로그인하셨습니다.</p>
                    <p>Oracle 데이터베이스와 연동이 완료되었습니다!</p>
                    <a href="/" class="btn">메인으로 돌아가기</a>
                    <a href="/api/members" class="btn">회원 목록 보기</a>
                </div>
            </div>
        </body>
        </html>
        """;
    }

    // API: 전체 회원 목록 JSON
    @GetMapping("/api/members")
    @ResponseBody
    public Map<String, Object> getMembers() {
        List<Member> members = memberService.findMembers();
        Map<String, Object> result = new HashMap<>();
        result.put("totalMembers", members.size());
        result.put("totalPets", memberService.getPetCount());
        result.put("members", members);
        return result;
    }

    // API: 통계 정보
    @GetMapping("/api/stats")
    @ResponseBody
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("memberCount", memberService.getMemberCount());
        stats.put("petCount", memberService.getPetCount());
        return stats;
    }
}