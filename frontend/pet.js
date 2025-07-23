let isLoggedIn = false;
let isRecording = false;
let isStreaming = false;
let currentUser = null;

// 모달 관리
function showLogin() {
  document.getElementById("loginModal").style.display = "flex";
}

function showRegister() {
  document.getElementById("registerModal").style.display = "flex";
}

function closeModal(modalId) {
  document.getElementById(modalId).style.display = "none";
}

// 로그인 처리
function login(event) {
  event.preventDefault();

  const email = document.getElementById("loginEmail").value;
  const password = document.getElementById("loginPassword").value;

  // 실제 구현에서는 서버 API 호출
  setTimeout(() => {
    currentUser = {
      email: email,
      name: "사용자",
      pet: {
        name: "멍멍이",
        breed: "골든 리트리버",
        age: 3,
        weight: 25,
      },
    };

    isLoggedIn = true;
    showDashboard();
    closeModal("loginModal");

    // 성공 알림
    alert("로그인 성공!");
  }, 1000);
}

// 회원가입 처리
function register(event) {
  event.preventDefault();

  const name = document.getElementById("registerName").value;
  const email = document.getElementById("registerEmail").value;
  const password = document.getElementById("registerPassword").value;
  const petName = document.getElementById("petName").value;
  const petBreed = document.getElementById("petBreed").value;

  // 실제 구현에서는 서버 API 호출
  setTimeout(() => {
    currentUser = {
      name: name,
      email: email,
      pet: {
        name: petName,
        breed: petBreed,
        age: 1,
        weight: 10,
      },
    };

    isLoggedIn = true;
    showDashboard();
    closeModal("registerModal");

    // 성공 알림
    alert("회원가입 성공! 자동으로 로그인됩니다.");
  }, 1000);
}

// 대시보드 표시
function showDashboard() {
  document.getElementById("mainContent").style.display = "none";
  document.getElementById("dashboard").classList.add("active");

  // 헤더 버튼 변경
  document.querySelector(".nav-buttons").innerHTML = `
                <button class="btn btn-secondary" onclick="logout()">로그아웃</button>
                <button class="btn btn-primary" onclick="showSettings()">설정</button>
            `;

  // 반려견 정보 업데이트
  if (currentUser && currentUser.pet) {
    updatePetInfo();
  }
}

// 반려견 정보 업데이트
function updatePetInfo() {
  const petCard = document.querySelector(".pet-details");
  if (petCard && currentUser.pet) {
    petCard.innerHTML = `
                    <h3>${currentUser.pet.name}</h3>
                    <p>${currentUser.pet.breed} • ${currentUser.pet.age}살</p>
                    <p>무게: ${currentUser.pet.weight}kg</p>
                    <div class="status-indicator status-online">
                        <div class="status-dot"></div>
                        온라인
                    </div>
                `;
  }
}

// 로그아웃
function logout() {
  isLoggedIn = false;
  currentUser = null;
  document.getElementById("mainContent").style.display = "grid";
  document.getElementById("dashboard").classList.remove("active");

  // 헤더 버튼 복원
  document.querySelector(".nav-buttons").innerHTML = `
                <button class="btn btn-secondary" onclick="showLogin()">로그인</button>
                <button class="btn btn-primary" onclick="showRegister()">회원가입</button>
            `;
}

// 스트리밍 토글
function toggleStream() {
  const btn = event.target;
  const videoPlaceholder = document.querySelector(".video-placeholder");

  if (!isStreaming) {
    isStreaming = true;
    btn.textContent = "스트리밍 중지";
    btn.classList.add("active");

    videoPlaceholder.innerHTML = `
                    <div style="font-size: 3rem; margin-bottom: 10px;">📹</div>
                    <p>실시간 영상 스트리밍 중...</p>
                    <small>연결됨 • 품질: HD</small>
                `;

    // 실제 구현에서는 WebRTC 연결
    console.log("스트리밍 시작");
  } else {
    isStreaming = false;
    btn.textContent = "스트리밍 시작";
    btn.classList.remove("active");

    videoPlaceholder.innerHTML = `
                    <div style="font-size: 3rem; margin-bottom: 10px;">📹</div>
                    <p>실시간 영상 스트리밍</p>
                    <small>목줄 연결 대기 중...</small>
                `;

    console.log("스트리밍 중지");
  }
}

// 스냅샷 촬영
function takeSnapshot() {
  if (isStreaming) {
    alert("스냅샷이 저장되었습니다!");
    console.log("스냅샷 촬영");
  } else {
    alert("스트리밍이 시작되지 않았습니다.");
  }
}

// 전체화면 토글
function toggleFullscreen() {
  const videoContainer = document.querySelector(".video-container");

  if (!document.fullscreenElement) {
    videoContainer.requestFullscreen();
  } else {
    document.exitFullscreen();
  }
}

// 음성 명령 전송
function sendCommand(command) {
  const commandNames = {
    sit: "앉아",
    stay: "기다려",
    come: "이리와",
    down: "엎드려",
    heel: "뒤로",
    stop: "멈춰",
  };

  alert(`"${commandNames[command]}" 명령을 전송했습니다!`);
  console.log(`음성 명령 전송: ${command}`);

  // 실제 구현에서는 WebSocket을 통한 명령 전송
}

// 음성 녹음 토글
function toggleRecording() {
  const recordBtn = document.querySelector(".record-btn");
  const recordIcon = document.getElementById("recordIcon");
  const recordText = document.getElementById("recordText");
  const recordingStatus = document.getElementById("recordingStatus");

  if (!isRecording) {
    isRecording = true;
    recordBtn.classList.add("recording");
    recordIcon.textContent = "⏹️";
    recordText.textContent = "녹음 중지";
    recordingStatus.textContent = "녹음 중... 명령어를 말씀해주세요";

    // 실제 구현에서는 Web Audio API 사용
    console.log("녹음 시작");
  } else {
    isRecording = false;
    recordBtn.classList.remove("recording");
    recordIcon.textContent = "🎙️";
    recordText.textContent = "녹음 시작";
    recordingStatus.textContent = "녹음이 완료되었습니다!";

    setTimeout(() => {
      recordingStatus.textContent = "";
    }, 3000);

    console.log("녹음 완료");
  }
}
