// ==============================
// FundSphere ERP Login Script
// ==============================

// Live Time & Date

function updateDateTime() {

    const now = new Date();

    const time = now.toLocaleTimeString('en-US', {
        hour12: true
    });

    const date = now.toLocaleDateString('en-US', {
        weekday: 'long',
        day: 'numeric',
        month: 'long',
        year: 'numeric'
    });

    const timeElement = document.getElementById("time");
    const dateElement = document.getElementById("date");

    if (timeElement) {
        timeElement.innerHTML = time;
    }

    if (dateElement) {
        dateElement.innerHTML = date;
    }

}

updateDateTime();

setInterval(updateDateTime, 1000);


// ==============================
// Show / Hide Password
// ==============================

const passwordInput = document.getElementById("password");
const togglePassword = document.querySelector(".toggle-password");

if (passwordInput && togglePassword) {

    togglePassword.addEventListener("click", function () {

        if (passwordInput.type === "password") {

            passwordInput.type = "text";

            this.innerHTML =
                '<i class="fa-solid fa-eye-slash"></i>';

        } else {

            passwordInput.type = "password";

            this.innerHTML =
                '<i class="fa-solid fa-eye"></i>';

        }

    });

}