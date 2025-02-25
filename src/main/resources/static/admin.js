const URL = "http://localhost:8080/api/admin";

let deleteModal = null;
let editModal = null;
let newUserModal = null;

let deleteUserId = null;
let editUserId = null;

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

function getAllUsers() {
    fetch(URL)
        .then(response => {
            if (!response.ok) {
                throw new Error("Failed to load data");
            }
            return response.json();
        })
        .then(users => {
            const usersTable = document.querySelector('.users-table');
            usersTable.innerHTML = '';

            users.forEach(userDTO => {
                const row = `
                    <tr>
                        <td>${userDTO.id}</td>
                        <td>${userDTO.username}</td>
                        <td>${userDTO.email}</td>
                        <td>${userDTO.roles.map(role => role.name.substring(5)).join(", ")}</td>
                        <td>
                            <button type="button" class="btn btn-primary" onclick="showEditModal(${JSON.stringify(userDTO).replace(/"/g, '&quot;')})">Edit</button>
                        </td>
                        <td>
                            <button type="button" class="btn btn-danger" onclick="showDeleteModal(${userDTO.id}, '${userDTO.username}', '${userDTO.email}', '${userDTO.roles.map(role => role.name.substring(5)).join(", ")}')">Delete</button>
                        </td>
                    </tr>
                `;
                usersTable.insertAdjacentHTML('beforeend', row);
            });
        })
        .catch(error => console.error("Error loading users:", error));
}

function showDeleteModal(userId, username, email, roles) {
    deleteUserId = userId;

    document.getElementById('deleteUserName').textContent = username;
    document.getElementById('deleteUserEmail').textContent = email;
    document.getElementById('deleteUserRoles').textContent = roles;

    if (!deleteModal) {
        deleteModal = new bootstrap.Modal(document.getElementById('deleteUserModal'));
    }
    deleteModal.show();
}

function deleteUser() {
    if (deleteUserId) {
        fetch(`${URL}/${deleteUserId}`, {
            method: 'DELETE',
        })
            .then(response => {
                if (response.ok) {
                    getAllUsers();
                } else {
                    alert("Error deleting user");
                }
            })
            .catch(error => console.error("Error deleting user:", error))
            .finally(() => {
                deleteModal.hide();
                deleteUserId = null;
            });
    }
}

function showEditModal(userDTO) {
    editUserId = userDTO.id;

    document.getElementById('editUserName').value = userDTO.username;
    document.getElementById('editUserPassword').value = '';

    const isAdmin = userDTO.roles.some(role => role.name === 'ROLE_ADMIN');
    document.getElementById('editUserIsAdmin').checked = isAdmin;

    if (!editModal) {
        editModal = new bootstrap.Modal(document.getElementById('editUserModal'));
    }
    editModal.show();
}

function saveUserChanges() {
    const updatedUser = {
        id: editUserId,
        username: document.getElementById('editUserName').value,
        password: document.getElementById('editUserPassword').value,
        roles: document.getElementById('editUserIsAdmin').checked ? [{ name: 'ROLE_ADMIN' }] : [{ name: 'ROLE_USER' }]
    };

    fetch(URL, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(updatedUser)
    })
        .then(response => {
            if (response.ok) {
                getAllUsers();
            } else {
                alert("Error updating user");
            }
        })
        .finally(() => {
            editModal.hide();
            editUserId = null;
        });
}

function showNewUserModal() {
    document.getElementById('newUserName').value = '';
    document.getElementById('newUserPassword').value = '';
    document.getElementById('newUserEmail').value = '';
    document.getElementById('newUserIsAdmin').checked = false;

    if (!newUserModal) {
        newUserModal = new bootstrap.Modal(document.getElementById('newUserModal'));
    }
    newUserModal.show();
}

function createNewUser() {
    const newUser = {
        username: document.getElementById('newUserName').value,
        password: document.getElementById('newUserPassword').value,
        email: document.getElementById('newUserEmail').value,
        roles: document.getElementById('newUserIsAdmin').checked ? [{ name: 'ROLE_ADMIN' }] : [{ name: 'ROLE_USER' }]
    };

    if (!newUser.username || !newUser.password || !newUser.email) {
        alert("Fill all fields");
        return;
    }

    fetch(URL, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(newUser)
    })
        .then(response => {
            if (response.ok) {
                getAllUsers();

                document.getElementById('newUserName').value = '';
                document.getElementById('newUserPassword').value = '';
                document.getElementById('newUserEmail').value = '';
                document.getElementById('newUserIsAdmin').checked = false;
            } else {
                alert("Error creating user");
            }
        })
        .catch(error => {
            console.error("Error creating user:", error);
            alert("Error creating user. Check console for details.");
        })
        .finally(() => {
            newUserModal.hide();
        });
}

document.addEventListener('DOMContentLoaded', getAllUsers);

loadUserInfo();

document.getElementById('confirmDeleteButton').addEventListener('click', deleteUser);
document.getElementById('saveEditButton').addEventListener('click', saveUserChanges);

document.getElementById('createNewUserButton').addEventListener('click', createNewUser);
document.getElementById('newUserButton').addEventListener('click', showNewUserModal);