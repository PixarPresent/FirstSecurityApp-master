async function loadUserInfo() {
    try {
        const response = await fetch(`http://localhost:8080/api/user`, { credentials: 'include' });
        const userDTO = await response.json();

        document.getElementById("userEmail").textContent = userDTO.email;
        document.getElementById("userRoles").textContent = userDTO.roles.map(role => role.name.replace("ROLE_", "")).join(', ');

        getInformationAboutUser(userDTO);
    } catch (error) {
        console.error("Failed to load user data");
    }
}

function getInformationAboutUser(userDTO) {
    const tableBody = document.getElementById('userTbody');
    let roles = userDTO.roles.map(role => role.name.replace("ROLE_", "")).join(', ');

    tableBody.innerHTML = `
        <tr>
            <td>${userDTO.id}</td>
            <td>${userDTO.username}</td>
            <td>${userDTO.email}</td>
            <td>${roles}</td>
        </tr>`;
}

loadUserInfo();