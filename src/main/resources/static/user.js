async function loadUserInfo() {
    try {
        const response = await fetch(`http://localhost:8080/api/user`, {credentials: 'include'})

        const user = await response.json()

        document.getElementById("userEmail").textContent = user.email
        document.getElementById("userRoles").textContent = user.roles.map(role => role.name.replace("ROLE_", "")).join(', ')

        getInformationAboutUser(user) 

    } catch (error) {
        console.error("загрузка данных не удалась")
    }
}

function getInformationAboutUser(user) {
    const tableBody = document.getElementById('userTbody');
    let roles = user.roles.map(role => role.name.replace("ROLE_", "")).join(', ')

    tableBody.innerHTML = `
        <tr>
            <td>${user.id}</td>
            <td>${user.username}</td>
            <td>${user.email}</td>
            <td>${roles}</td>   
        </tr>`
}

loadUserInfo();