const URL = "http://localhost:4567/student"

let table = document.getElementById("students-table")

let clearTable = () => {
    table.innerHTML = 
    `<tr>
        <th>Id</th>
        <th>Имя</th>
        <th>Фамилия</th>
        <th>Отчество</th>
        <th>Дата рождения</th>
        <th>Группа</th>
        <th>Действия</th>
    </tr>`
}

let fetchStudents = () => {
    fetch(URL)
        .then( res => res.json() )
        .then( data => {
            clearTable()

            data.forEach(student => {
                let tr = document.createElement("tr")

                let idTd = document.createElement("td")
                idTd.innerHTML = student.id
                tr.appendChild(idTd)

                let nameTd = document.createElement("td")
                nameTd.innerHTML = student.name
                tr.appendChild(nameTd)

                let surnameTd = document.createElement("td")
                surnameTd.innerHTML = student.surname
                tr.appendChild(surnameTd)

                let lastnameTd = document.createElement("td")
                lastnameTd.innerHTML = student.lastname
                tr.appendChild(lastnameTd)

                let birthTd = document.createElement("td")
                birthTd.innerHTML = new Date(student.birth).toLocaleDateString('ru-RU')
                tr.appendChild(birthTd)

                let groupTd = document.createElement("td")
                groupTd.innerHTML = student.group
                tr.appendChild(groupTd)

                let deleteTd = document.createElement("td")
                deleteTd.innerHTML = `<a href='#' onclick='deleteStudent(${student.id})'>Удалить</a>`
                tr.appendChild(deleteTd)

                table.appendChild(tr)
            })
        })
}

let deleteStudent = (id) => {
    fetch(URL + "/" + id, {method: "DELETE"})
    .then( res => { 
        fetchStudents()
    })
}

let form = document.forms[0]
form.onsubmit = (e) => {
    e.preventDefault()

    fetch(URL, {
        method: "POST",
        mode: 'no-cors',
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(Object.fromEntries(new FormData(form)))
    })
    .then( res => { 
        fetchStudents()
        form.reset()
    })
}

clearTable()
fetchStudents()