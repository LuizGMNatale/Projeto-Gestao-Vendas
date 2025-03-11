document.addEventListener("DOMContentLoaded", function () {
    // Login
    const loginForm = document.getElementById("loginForm");
    if (loginForm) {
        loginForm.addEventListener("submit", async function (event) {
            event.preventDefault();

            const username = document.getElementById("username").value;
            const password = document.getElementById("password").value;

            const response = await fetch("http://localhost:8080/auth/login", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ username, password }),
                credentials: "include"
            });

            const data = await response.json();

            if (response.ok) {
                window.location.href = data.redirect;
            } else {
                alert("Usuário ou senha incorretos.");
            }
        });
    }

    // Logout
    const logoutBtn = document.getElementById("logoutBtn");
    if (logoutBtn) {
        logoutBtn.addEventListener("click", async function (event) {
            event.preventDefault();

            const response = await fetch("http://localhost:8080/auth/logout", {
                method: "GET",
                credentials: "include"
            });

            if (response.ok) {
                const data = await response.json();
                alert(data.message);
                window.location.href = data.redirect;
            } else {
                console.error("Erro ao fazer logout.");
            }
        });
    }
});
