/**
 * Ryan Nguyen
 * main.js
 * functionaility for main.html. Client-side js makes ajax to server.js grab information and display to user
 */


var userID = sessionStorage.getItem("user");

$(main);
console.log("USERID " + userID);


function main(){
    loadUser();
    $("#addBtn").click(popupWindow);
    $("#createTaskBtn").click(createTask);
}


/**
 * Loads all of users tasks by making a call to end point /jointables
 */
function loadUser(){
    //ajax call to grab user name for welcome header
    $.ajax({
        url:"http://augwebapps.com:3132/getname?user_num=" + userID,
        method: "GET"
    }).done(function(data){
        let clientName = JSON.parse(data)[0];

        //updating welcome header to include user name.
        $("#mainMain > h1").html("Hello, " + clientName.name + "!");
    });

/*------------------------- Second ajax call to grab users task by innerjoin both tables ------------------------------*/

    $.ajax({
        url:"http://augwebapps.com:3132/jointables?user_num=" + userID,
        method: "GET"
    }).done(function(data){
        console.log("TABLE HAS BEEN JOINED");
        let temp = JSON.parse(data)

        let length = Object.keys(temp).length;
        
        for(let i = 0; i < length;i++){
            //iterates over all tasks that we got from the SQL
            let obj = JSON.parse(data)[i];

        
                let dueDate = obj.due_date;
                let taskName = obj.user_task;
                let compstatus = obj.completed;
            

                //date objects for comparison
                let date = new Date().setHours(0,0,0,0);
                let due = new Date(dueDate);

                console.log("DATE: " + date);

                if(compstatus == 1){
                    let newDate = dueDate.slice(0, 10);

                    $("#pastTasks").append("<div id='tasks'> <h3>" + taskName + "</h3> <p>" + newDate + 
                    "</p>" + "<div id='tasksFunction'> <span id='checkmark'><ion-icon name='checkmark-circle-outline'></ion-icon></span> <span id='trashcan'><ion-icon name='trash-bin-outline'></ion-icon></span></div>");
                
                }
                else if(due < date){
                    //appending new div if the task is overdue
                    console.log("LATE");
                    let newDate = dueDate.slice(0, 10);
                  
                    //APPEND DIV TO DISPLAY TASKS
                    $("#overdueColumns").append("<div id='tasks'> <h3>" + taskName + "</h3> <p>" + newDate + 
                    "</p>" + "<div id='tasksFunction'> <span id='checkmark'><ion-icon name='checkmark-circle-outline'></ion-icon></span> <span id='trashcan'><ion-icon name='trash-bin-outline'></ion-icon></span></div>");
                    

                }else if(due >= date){
                    //Task is not overdue and will go into task columns
                    console.log("NOT LATE");
                    let newDate = dueDate.slice(0, 10);

                    $("#inprogress-Tasks").append("<div id='tasks'> <h3>" + taskName + "</h3> <p>" + newDate + 
                    "</p>" + "<div id='tasksFunction'> <span id='checkmark'><ion-icon name='checkmark-circle-outline'></ion-icon></span> <span id='trashcan'><ion-icon name='trash-bin-outline'></ion-icon></span></div>")
                }
        }//end of adding all tasks

        //makesure to load event listener for every event
        console.log("ADDING LISTNERS FOR CHECKS & TRASHCAN")
        $("#checkmark > ion-icon").click(taskComplete);
        $("#trashcan > ion-icon").click(deleteTask);
    });



}



/**
 * Create task is function that calls in ajax call towards endpoint /maketask that will create a new task in the task table
 */
function createTask(){
    //grab data from user input
    let name = $("#taskInput").val();
    let dueDate = $("#dateInput").val();

    //ajax call to maketask endpoint in server.js
    $.ajax({
        url: "http://augwebapps.com:3132/maketask?user_id=" + userID + "&taskname=" + name + "&date=" + dueDate,
        method: "GET"
    }).done(addingTask);
        
}

/**
 * Response to creating task function that will create new task div
 * @param {*} data 
 */
function addingTask(data){
    console.log("ADDING TASKING...")
    //grabbing information from input
    let name = $("#taskInput").val();
    let dueDate = $("#dateInput").val();

    let obj = JSON.parse(data);

    

    if(obj.message == "Task added"){

        //date objects for comparison
        let date = new Date().setHours(0,0,0,0);
        let due = new Date(dueDate);

        
        if(due < date){
            //appending new div if the task is overdue
            console.log("ADDED IN OVERDUE");
                  
            //APPEND DIV TO DISPLAY TASKS
            $("#overdueColumns").append("<div id='tasks'> <h3>" + name + "</h3> <p>" + dueDate + 
            "</p>" + "<div id='tasksFunction'> <span id='checkmark'><ion-icon name='checkmark-circle-outline'></ion-icon></span> <span id='trashcan'><ion-icon name='trash-bin-outline'></ion-icon></span></div>");
                    
            closePopup();

        }else if(due >= date){
            //Task is not overdue and will go into task columns
            console.log("ADDED IN TASK");
                

            $("#inprogress-Tasks").append("<div id='tasks'> <h3>" + name + "</h3> <p>" + dueDate + 
            "</p>" + "<div id='tasksFunction'> <span id='checkmark'><ion-icon name='checkmark-circle-outline'></ion-icon></span> <span id='trashcan'><ion-icon name='trash-bin-outline'></ion-icon></span></div>");
            closePopup();
        }
    } 
    
    //load event listener for all event
    console.log("ADDING LISTNERS FOR CHECKS & TRASHCAN")
    $("#checkmark > ion-icon").click(taskComplete);
    $("#trashcan > ion-icon").click(deleteTask);
            
}

/**
 * ajax call to move task from overdue or in-progress to done. Also changes the database to make sure those tasks are marked as done.
 */
function taskComplete(){
    console.log("ALL DONE WITH THIS TASK");
    let taskName = $(this).closest("#tasks").find("h3").html();
    let parent = $(this).closest("#tasks").detach();


   console.log("TASK NAME: " + taskName);

    
    $.ajax({
        url: "http://augwebapps.com:3132/taskcompleted?user_id=" + userID + "&userTask=" + taskName,
        method: "GET"
    }).done(function(data){
        let obj = JSON.parse(data);
        console.log("MESSAGE from task complete: " +  obj.message);

        if(obj.message === "task has been updated"){
            //appending div to done
            $("#pastTasks").append(parent);
        }else{
            console.log("Completed failed");
        }
    })
}

/**
 * Ajax call to delete from database and screen
 */
function deleteTask(){
    //taskname
    let taskName = $(this).closest("#tasks").find("h3").html();


    //ajax call to end point delete
    $.ajax({
        url: "http://augwebapps.com:3132/byetask?user_id=" + userID + "&userTask=" + taskName,
        method: "GET"
    }).done(function(data){
        let obj = JSON.parse(data);
        //check if item has been delete
        if(obj.message === "deleted"){
            console.log("Sucessfully delete task");
        }else{
            console.log("Failed at deleting");
            return;
        }
    })

    //remove item from DOM
    $(this).closest("#tasks").remove();
}


/**
 * Opens popup window
 */
function popupWindow(){
    //openning up popup
    $("#popup").css("display", "block");
    $("#mainMain").addClass("activeBlur");

    //close button
    $("#closeSpan").click(closePopup);
}


/**
 * closes popup window
 */
function closePopup(){
    $("#popup").css("display", "none");
    $("#mainMain").removeClass("activeBlur");
}


