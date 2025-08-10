// React 라이브러리를 동적으로 로드
function loadReact() {
  // React 라이브러리 로드
  const reactScript = document.createElement("script");
  reactScript.crossOrigin = "anonymous";
  reactScript.src = "https://unpkg.com/react@18/umd/react.development.js";

  const reactDomScript = document.createElement("script");
  reactDomScript.crossOrigin = "anonymous";
  reactDomScript.src =
    "https://unpkg.com/react-dom@18/umd/react-dom.development.js";

  const babelScript = document.createElement("script");
  babelScript.src = "https://unpkg.com/@babel/standalone/babel.min.js";

  // 순서대로 로드
  document.head.appendChild(reactScript);
  document.head.appendChild(reactDomScript);
  document.head.appendChild(babelScript);

  // 모든 스크립트 로드 완료 후 React 컴포넌트 실행
  babelScript.onload = function () {
    initReactComponents();
  };
}

function initReactComponents() {
  // React 컴포넌트 코드를 문자열로 생성
  const reactCode = `
        const { createRoot } = ReactDOM;
        const { useState } = React;
        
        function MyReactApp() {
            const [count, setCount] = useState(0);
            
            return React.createElement('div', { style: { padding: '20px', border: '1px solid #ccc' } },
                React.createElement('h2', null, 'React Component'),
                React.createElement('p', null, 'Count: ' + count),
                React.createElement('button', 
                    { onClick: () => setCount(count + 1) }, 
                    'Click me!'
                )
            );
        }
        
        // 기존 페이지에 React 컨테이너 추가
        const container = document.createElement('div');
        container.id = 'react-container';
        document.body.appendChild(container);
        
        const root = createRoot(container);
        root.render(React.createElement(MyReactApp));
    `;

  // Babel로 JSX 변환 및 실행
  const transformedCode = Babel.transform(reactCode, {
    presets: ["react"],
  }).code;
  eval(transformedCode);
}

// React 로드 함수 호출
loadReact();
