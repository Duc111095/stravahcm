function fetchUserInfo() {
    fetch("/runnerhcm/user_info")
        .then(response => response.json())
        .then(response => {
            if (response.message == "success") {
                const name = response.data.first_name + " " + response.data.last_name;
                // Add user info to the page
                const sidebar = document.getElementById('sidebar');
                if (sidebar) {
                    const userInfo = document.createElement('div');
                    userInfo.href = "#";
                    userInfo.innerText = `Welcome, ${name}!`;
                    userInfo.classList.add('user-info');
                    sidebar.insertBefore(userInfo, sidebar.firstChild);
                }
            }
        })
        .catch(error => console.error("Error fetching user info:", error));
}

window.addEventListener("load", function() {
    fetchUserInfo();
})

function sortTable(colIndex, type) {
    const table = document.getElementById('summary');
    const tbody = document.getElementById('event-summary-body')
    const rows = Array.from(tbody.querySelectorAll('tr'));
    const isAsc = table.getAttribute('data-sort-dir') !== 'asc';
    table.setAttribute('data-sort-dir', isAsc ? 'asc' : 'desc');
    rows.sort((a, b) => {
        const valA = a.cells[colIndex].innerText.trim();
        const valB = b.cells[colIndex].innerText.trim();
        const aVal = type === 'num' ? parseFloat(valA) || 0 : valA;
        const bVal = type === 'num' ? parseFloat(valB) || 0 : valB;
        return isAsc ? aVal - bVal : bVal - aVal;
    });
    var index = 0;
    for (let i = 0; i < rows.length; i++) {
        var row = rows[i]
        row.querySelector("td:first-child").textContent = ++index;
        tbody.appendChild(row);
    }   
}
