async function loadUserInfo() {
    try {
        const response = await fetch(`http://localhost:8080/api/user`, { credentials: 'include' });
        const userDTO = await response.json();

        document.getElementById("userEmailBottom").textContent = userDTO.email;

        getInformationAboutUser(userDTO);
    } catch (error) {
        console.error("Failed to load user data");
    }
}

function getInformationAboutUser(userDTO) {
    const cardsContainer = document.getElementById('userInfoCards');
    let roles = userDTO.roles.map(role => role.name.replace("ROLE_", "")).join(', ');

    cardsContainer.innerHTML = `
        <div class="col-md-6 col-lg-3">
            <div class="info-card">
                <div class="info-label">
                    <i class="bi bi-hash me-1"></i>User ID
                </div>
                <div class="info-value">#${userDTO.id}</div>
            </div>
        </div>
        <div class="col-md-6 col-lg-3">
            <div class="info-card">
                <div class="info-label">
                    <i class="bi bi-person me-1"></i>Username
                </div>
                <div class="info-value">${userDTO.username}</div>
            </div>
        </div>
        <div class="col-md-6 col-lg-3">
            <div class="info-card">
                <div class="info-label">
                    <i class="bi bi-envelope me-1"></i>Email
                </div>
                <div class="info-value" style="font-size: 1.1rem;">${userDTO.email}</div>
            </div>
        </div>
        <div class="col-md-6 col-lg-3">
            <div class="info-card">
                <div class="info-label">
                    <i class="bi bi-shield-check me-1"></i>Role
                </div>
                <div class="info-value role">${roles}</div>
            </div>
        </div>`;
}

loadUserInfo();
