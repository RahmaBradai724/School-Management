const API_URL = '/api';
let currentUser = null;
let token = localStorage.getItem('token');

// Initialize App
document.addEventListener('DOMContentLoaded', () => {
    if (token) {
        verifyTokenAndInit();
    } else {
        showAuth();
    }

    // Login Form
    document.getElementById('login-form').addEventListener('submit', handleLogin);

    // Profile Form
    document.getElementById('profile-form').addEventListener('submit', handleProfileUpdate);

    // Password Form
    document.getElementById('password-form').addEventListener('submit', handlePasswordChange);
});

async function verifyTokenAndInit() {
    try {
        const response = await fetch(`${API_URL}/profile`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (response.ok) {
            currentUser = await response.json();
            showDashboard();
        } else {
            logout();
        }
    } catch (error) {
        console.error('Initial error:', error);
        logout();
    }
}

async function handleLogin(e) {
    e.preventDefault();
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const errorDiv = document.getElementById('login-error');

    try {
        const response = await fetch(`${API_URL}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });

        const data = await response.json();

        if (response.ok) {
            token = data.token;
            localStorage.setItem('token', token);
            currentUser = { username: data.username, role: data.role };
            showDashboard();
            showToast('Login successful!');
        } else {
            errorDiv.textContent = data.message || 'Login failed';
            errorDiv.classList.remove('d-none');
        }
    } catch (error) {
        errorDiv.textContent = 'Connection error';
        errorDiv.classList.remove('d-none');
    }
}

function logout() {
    localStorage.removeItem('token');
    token = null;
    currentUser = null;
    showAuth();
}

function showAuth() {
    document.getElementById('auth-container').classList.remove('d-none');
    document.getElementById('app-container').classList.add('d-none');
}

function showDashboard() {
    document.getElementById('auth-container').classList.add('d-none');
    document.getElementById('app-container').classList.remove('d-none');
    document.getElementById('user-display-name').textContent = currentUser.username;

    showSection('dashboard');
    loadEnrollments();
}

function showSection(sectionId) {
    document.querySelectorAll('.app-section').forEach(s => s.classList.add('d-none'));
    document.getElementById(`${sectionId}-section`).classList.remove('d-none');

    // Update nav active state
    document.querySelectorAll('.nav-link').forEach(l => {
        l.classList.remove('active');
        if (l.textContent.toLowerCase() === sectionId) l.classList.add('active');
    });

    if (sectionId === 'courses') loadAllCourses();
    if (sectionId === 'profile') loadProfileData();
}

async function loadEnrollments() {
    try {
        // First get student profile to get the student object ID if not known
        const profileRes = await fetch(`${API_URL}/profile`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        const profile = await profileRes.json();

        // Use student ID if role is STUDENT
        const url = profile.role === 'STUDENT' ? `${API_URL}/enrollments/student/${profile.id}` : `${API_URL}/enrollments`;

        const response = await fetch(url, {
            headers: { 'Authorization': `Bearer ${token}` }
        });

        const enrollments = await response.json();
        renderEnrollments(enrollments);
        document.getElementById('stat-courses').textContent = enrollments.length;
    } catch (error) {
        console.error('Error loading enrollments:', error);
    }
}

function renderEnrollments(enrollments) {
    const container = document.getElementById('my-enrollments');
    if (!enrollments || enrollments.length === 0) {
        container.innerHTML = '<div class="col-12 text-center text-white-50 py-5"><p>You are not enrolled in any courses yet.</p></div>';
        return;
    }

    container.innerHTML = enrollments.map(enr => `
        <div class="col-md-6 col-lg-4 animate-in">
            <div class="glass-card p-4 h-100 course-card">
                <div class="d-flex justify-content-between mb-3">
                    <span class="badge bg-primary text-uppercase">${enr.course.courseCode}</span>
                    <span class="status-badge bg-${getStatusColor(enr.status)}">${enr.status}</span>
                </div>
                <h5 class="text-white mb-2">${enr.course.courseName}</h5>
                <p class="text-white-50 small mb-3">${enr.course.description || 'No description available.'}</p>
                <div class="mt-auto d-flex align-items-center">
                    <i class="bi bi-person-circle me-2 text-primary"></i>
                    <span class="small text-white-50">${enr.course.teacher ? enr.course.teacher.firstName + ' ' + enr.course.teacher.lastName : 'Unassigned'}</span>
                </div>
            </div>
        </div>
    `).join('');
}

async function loadAllCourses() {
    try {
        const response = await fetch(`${API_URL}/courses`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        const courses = await response.json();
        renderAllCourses(courses);
    } catch (error) {
        console.error('Error loading courses:', error);
    }
}

function renderAllCourses(courses) {
    const container = document.getElementById('all-courses');
    const isStudent = currentUser && currentUser.role === 'STUDENT';

    container.innerHTML = courses.map(course => `
        <div class="col-md-6 col-lg-4 animate-in">
            <div class="glass-card p-4 h-100 course-card">
                <div class="d-flex justify-content-between mb-3">
                    <span class="badge bg-success text-uppercase">${course.courseCode}</span>
                    <span class="badge bg-info">${course.credits} Credits</span>
                </div>
                <h5 class="text-white mb-2">${course.courseName}</h5>
                <p class="text-white-50 small mb-4">${course.description || 'No description available.'}</p>
                <div class="mt-auto">
                    ${isStudent ? `
                    <button class="btn btn-outline-primary btn-sm w-100" onclick="enroll(${course.id})">
                        <i class="bi bi-plus-lg me-1"></i> Enroll Now
                    </button>
                    ` : ''}
                </div>
            </div>
        </div>
    `).join('');
}

async function enroll(courseId) {
    try {
        const response = await fetch(`${API_URL}/enrollments/self/enroll?courseId=${courseId}`, {
            method: 'POST',
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (response.ok) {
            showToast('Enrolled successfully! A confirmation email has been sent.');
            showSection('dashboard');
            loadEnrollments();
        } else {
            const error = await response.json();
            showToast(error.message || 'Enrollment failed', 'danger');
        }
    } catch (error) {
        showToast('Connection error', 'danger');
    }
}

async function loadProfileData() {
    try {
        const response = await fetch(`${API_URL}/profile`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        const profile = await response.json();

        document.getElementById('profile-firstName').value = profile.firstName || '';
        document.getElementById('profile-lastName').value = profile.lastName || '';
        document.getElementById('profile-email').value = profile.email || '';
    } catch (error) {
        console.error('Error loading profile:', error);
    }
}

async function handleProfileUpdate(e) {
    e.preventDefault();
    const data = {
        firstName: document.getElementById('profile-firstName').value,
        lastName: document.getElementById('profile-lastName').value,
        email: document.getElementById('profile-email').value
    };

    try {
        const response = await fetch(`${API_URL}/profile`, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (response.ok) {
            showToast('Profile updated!');
        } else {
            showToast('Update failed', 'danger');
        }
    } catch (error) {
        showToast('Connection error', 'danger');
    }
}

async function handlePasswordChange(e) {
    e.preventDefault();
    const data = {
        currentPassword: document.getElementById('current-password').value,
        newPassword: document.getElementById('new-password').value
    };

    try {
        const response = await fetch(`${API_URL}/profile/password`, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (response.ok) {
            showToast('Password updated!');
            e.target.reset();
        } else {
            const error = await response.json();
            showToast(error.message || 'Update failed', 'danger');
        }
    } catch (error) {
        showToast('Connection error', 'danger');
    }
}

function getStatusColor(status) {
    switch (status) {
        case 'ACTIVE': return 'success';
        case 'PENDING': return 'warning';
        case 'COMPLETED': return 'primary';
        case 'DROPPED': return 'danger';
        default: return 'secondary';
    }
}

function showToast(message, type = 'success') {
    const toastEl = document.getElementById('liveToast');
    const toastMsg = document.getElementById('toast-message');

    toastEl.classList.remove('bg-primary', 'bg-danger');
    toastEl.classList.add(type === 'danger' ? 'bg-danger' : 'bg-primary');

    toastMsg.textContent = message;
    const toast = new bootstrap.Toast(toastEl);
    toast.show();
}
