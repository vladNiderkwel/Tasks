const URL = "http://localhost:4567"

let taskContainer = document.getElementById("task-container")

let clearTaskContainer = () => {
    taskContainer.innerHTML = ""
}

let tasks = []
let displayingTasks = []

let updateTasks = () => {
    clearTaskContainer()

    displayingTasks.forEach(task => {
        let taskCard = document.createElement("div")
        taskCard.className = "task-card"

        let title = task.name

        if (task.status) title = "✔ " + title

        taskCard.innerHTML = `
            <p>${title}</p>
            <div>${task.shortDesc}</div>
            <p>${new Date(task.date).toLocaleString("ru-RU")}</p>
        `
        taskCard.addEventListener("click", e => { showTask(task) })

        taskContainer.appendChild(taskCard)
    })
}

let fetchTasks = () => {
    fetch(`${URL}/`)
        .then(resp => resp.json())
        .then(data => {
            tasks = data
            displayingTasks = tasks

            updateTasks()
        })
}

fetchTasks()

/*
// Поиск по строке
*/

let findTasks = []

let searchResultsContainer = document.getElementById("search-results-container")

let fetchFind = (query) => {
    fetch(`${URL}/find?q=${query}`)
        .then(resp => resp.json())
        .then(data => {
            findTasks = data
            displayingTasks = findTasks

            updateTasks()
        })
}

let formSearch = document.getElementById("search-form")
formSearch.onsubmit = (e) => {
    e.preventDefault()

    fetchFind(formSearch.elements.query.value)
}

let isSearchResultsContainerVisible = false 

document.getElementById("search-input").addEventListener("input", e => {
    fetch(`${URL}/find?q=${formSearch.elements.query.value}&limit=5&offset=0`)
        .then(resp => resp.json())
        .then(data => {
            isSearchResultsContainerVisible = true

            findTasks = data

            searchResultsContainer.innerHTML = ""
            searchResultsContainer.style.visibility = "visible"

            findTasks.forEach(task => {
                let findTaskCard = document.createElement("div")
                findTaskCard.className = "find-task-card"
                findTaskCard.innerHTML = `
                    <p>${task.name}</p>
                    <div>${task.shortDesc}</div>
                    <p>${new Date(task.date).toLocaleString("ru-RU")}</p>
                `

                let divider = document.createElement("div")
                divider.className = "divider"

                searchResultsContainer.appendChild(findTaskCard)
                searchResultsContainer.appendChild(divider)

                findTaskCard.addEventListener("click", e => {
                    showTask(task)

                    searchResultsContainer.style.visibility = "hidden"
                })
            })

            searchResultsContainer.removeChild(searchResultsContainer.lastChild)  
        })
})

window.addEventListener("click", e => {
    if (isSearchResultsContainerVisible && !searchResultsContainer.contains(e.target)){
        isSearchResultsContainerVisible = false
        searchResultsContainer.style.visibility = "hidden"
    }
})

/*
// Вывод невыполненных задач
*/

let isTasksNotDone = document.getElementById("tasks-not-done")
isTasksNotDone.addEventListener("change", e => {
    if (e.currentTarget.checked) {

        let filtered = tasks.filter(task => task.status == false)
        displayingTasks = filtered
    }
    else displayingTasks = tasks

    updateTasks()
})

/*
// Поиск по дате
*/

let fetchDate = (from, to) => {
    let url = `${URL}/date?from=${from}&to=${to}`

    if (isTasksNotDone.checked) url += "&status=false"

    fetch(url)
        .then(resp => resp.json())
        .then(data => {
            tasks = data
            displayingTasks = tasks
            
            updateTasks()
        })
}

document.getElementById("find-today").addEventListener("click", e => {
    let today = new Date()
    today.setHours(0, 0, 0)

    let midnight = new Date()
    midnight.setHours(23, 59, 59)

    fetchDate(today.getTime(), midnight.getTime())
})

document.getElementById("find-week").addEventListener("click", e => {
    let today = new Date()
    today.setHours(0, 0, 0)

    let thisWeek = new Date()
    thisWeek.setDate(today.getDate() + 6)
    thisWeek.setHours(23, 59, 59)

    fetchDate(today.getTime(), thisWeek.getTime())
})

/*
// Календарь
*/

let selectedDates = []

let calendarDate = new Date()

let calendarText = document.getElementById("calendar-text")
let calendarTable = document.getElementById("calendar-table")

let loadCalendar = () => {

    clearCalendarTable()

    let month = calendarDate.toLocaleString('ru-RU', { month: 'long' }).toUpperCase()
    let year = calendarDate.getFullYear()

    calendarText.innerHTML = `${month} ${year}`

    let firstDay = new Date(calendarDate.getFullYear(), calendarDate.getMonth(), 1)
    let lastDay = new Date(calendarDate.getFullYear(), calendarDate.getMonth() + 1, 0)

    let i = 0
    let tr = document.createElement("tr")

    if (firstDay.getDay() != 1)
        for (let index = 0; index < firstDay.getDay() - 1; index++) {
            let td = document.createElement("td")
            td.innerHTML = ""
            tr.appendChild(td)
            i++
        }

    for (let d = firstDay; d.getTime() <= lastDay.getTime(); d.setDate(d.getDate() + 1)) {

        i++

        let date = new Date(d.getFullYear(), d.getMonth(), d.getDate())

        let td = document.createElement("td")
        td.classList.add("selectable-date")
        td.innerHTML = date.getDate()

        if (selectedDates.includes(date.getTime()))
            td.classList.add("selected-date")

        td.addEventListener("click", e => {

            if (selectedDates.includes(date.getTime())) {
                td.classList.remove("selected-date")
                selectedDates = selectedDates.filter(time => time != date.getTime())
            }
            else {
                td.classList.add("selected-date")
                selectedDates.push(date.getTime())
            }

            if (selectedDates.length > 0) {
                let from = new Date(getMinSelectedDate())
                from.setHours(0, 0, 0)

                let to = new Date(getMaxSelectedDate())
                to.setHours(23, 59, 59)

                fetchDate(from.getTime(), to.getTime())
            }
            else fetchTasks()
        })

        tr.appendChild(td)

        if (i == 7 || date.getTime() == lastDay.getTime()) {
            calendarTable.appendChild(tr)
            tr = document.createElement("tr")
            i = 0
        }
    }
}

let previousMonth = () => {
    changeMonth(-1)
    loadCalendar()
}

let nextMonth = () => {
    changeMonth(1)
    loadCalendar()
}

let clearCalendarTable = () => {
    calendarTable.innerHTML =
        `<tr>
            <th>Пн</th>
            <th>Вт</th>
            <th>Ср</th>
            <th>Чт</th>
            <th>Пт</th>
            <th>Сб</th>
            <th>Вс</th>
        </tr>`
}

let changeMonth = (change) => {
    calendarDate.setMonth(calendarDate.getMonth() + change)
}

document.getElementById("calendar-previous").addEventListener("click", e => {
    previousMonth()
})

document.getElementById("calendar-next").addEventListener("click", e => {
    nextMonth()
})

let getMaxSelectedDate = () => {
    if (selectedDates.length == 0) return null

    let max = selectedDates[0]

    selectedDates.forEach(time => {
        if (max < time) max = time
    })

    return max
}

let getMinSelectedDate = () => {
    if (selectedDates.length == 0) return null

    let min = selectedDates[0]

    selectedDates.forEach(time => {
        if (min > time) min = time
    })

    return min
}

loadCalendar()

/*
// Сортировка
*/

document.getElementById("sort-tasks").addEventListener("click", e => {
    displayingTasks.sort((a, b) =>
        new Date(a.date).getTime() - new Date(b.date).getTime()
    )

    updateTasks()
})

/*
// Отображение всей информации о задании
*/

let todoBackground = document.getElementById("todo-background")
let todoCardTitle = document.getElementById("todo-card-title")
let todoCardDate = document.getElementById("todo-card-date")
let todoCardDescription = document.getElementById("todo-card-description")

let showTask = (task) => {
    todoBackground.style.visibility = "visible"

    let title = task.name
    if (task.status) title = "✔ " + title
    todoCardTitle.innerHTML = title

    todoCardDate.innerHTML = new Date(task.date).toLocaleString("ru-RU")
    todoCardDescription.innerHTML = task.fullDesc
}

let hideTask = () => {
    todoBackground.style.visibility = "hidden"
}

document.getElementById("todo-card-done").addEventListener("click", e => hideTask())