const assignments = [
    {
        id: 1,
        title: "Deploy EC2 Instance",
        desc: "Create and configure an EC2 instance",
        due: "2026-05-20"
    },
    {
        id: 2,
        title: "S3 Bucket Configuration",
        desc: "Set up static website hosting",
        due: "2026-05-27"
    }
];

function calculateDaysLeft(dueDate) {
    const today = new Date();
    const due = new Date(dueDate);
    const diff = Math.ceil((due - today) / (1000 * 60 * 60 * 24));
    return diff;
}

function renderAssignments() {
    const container = document.getElementById("assignmentList");

    assignments.forEach(a => {
        const daysLeft = calculateDaysLeft(a.due);

        container.innerHTML += `
            <div class="assignment-card">
                <div class="left">
                    <div class="title">${a.title}</div>
                    <div class="desc">${a.desc}</div>

                    <div class="meta">
                        <span>📅 Due: ${a.due}</span>
                        <span class="badge">${daysLeft} days left</span>
                    </div>
                </div>

                <div class="right">
                    <button onclick="submitAssignment(${a.id})">
                        ⬆ Submit
                    </button>
                </div>
            </div>
        `;
    });
}

function submitAssignment(id) {
    alert("Submit assignment ID: " + id);

    // later we connect to backend:
    // fetch('/api/assignments/submit')
}

renderAssignments();