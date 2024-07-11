/**
 * Ryan Nguyen
 * server.js
 * run an express server with multiple endpoints for queries
 */


var express = require("express");
var mysql = require("mysql2");
var app = express();
 
 app.use(express.static("public"));
 
 // End points and function

 //checking if user exist
app.get("/userlogin", getUser);

//creating a new user
app.get("/createuser", creatingUser);

//grabbing user name
app.get("/getname", grabName);

//joining tables to grab task
app.get("/jointables", grabAllTasks);

//creating task for the task table
app.get("/maketask", createTask);

//alter task tabe 
app.get("/taskcompleted", completeTask);

app.get("/byetask", deleteTask);
//Response Functions


/**
 * CREATES A NEW USER INSIDE OF THE USER TABLE
 * @param {*} req 
 * @param {*} res 
 */
function creatingUser(req, res){
    console.log("CREATING USERS...");

    let name = req.query.name;
    let username = req.query.username;
    let password = req.query.password;

    console.log("NAME: " + name);
    console.log("USERNAME: " + username);
    console.log("PASSWORD: " + password);

    //creating connection for mysql
    var conn = mysql.createConnection({
        host: "127.0.0.1",
        user: "nguyenrr",
        password: "$311ry559",
        database: "nguyen_tasks"
    });

    conn.connect(function(err){
        if(err) throw err;
        console.log("MYSQL CONNECTED.")
    });

    const date = new Date();
    let year = date.getFullYear();
    let month = (date.getMonth() + 1).toString().padStart(2, '0');
    let day = date.getDate();

    let fullDate = year + "-" + month + "-" + day;


    //SQL queries
    let sql = "insert into users (user_name, password, name, date_joined) VALUES " + "(" + '"' + username + '",' + '"' + password + '",' + '"' + name + '",' + '"' + fullDate + '");';
    let sql2 = "select user_id from users where user_name = " + '"' + username + '"' + " AND password = " + '"' + password + '" AND name = ' + '"' + name + '"';

  
    //Check if that account with that username and password exist already
    conn.query(sql2, function(err, result){
        if(err){
    
        }else{
            let obj = JSON.stringify(result);
            let newObj = JSON.parse(obj)[0];
        
            
            if(typeof newObj === "undefined"){
                //if newObj is undefined there is no user_id inside and no user exist
                console.log("NO USER");

                //since no user exist we're allowed to create one
                conn.query(sql, function(eer){
                    if(eer){
                        //error user was added
                        console.log("FAILED")
                    }else{
                        console.log("User has been added");
                        
                        //grabbing the ID of new user to be sent back to client side
                        conn.query(sql2, function(eer, result){
                            if(eer){
                                console.log("FAILED GRABBING ID OF NEWLY CREATED USER")
                            }else{
                                res.send(JSON.stringify(result));
                            }
                        });

                    }
                })
            }else{
                //account already exist in database so user has to select a new username or Password
                console.log("USER EXIST: " + newObj.user_id + " Sending back results...");
                

                res.send({response: "User already exist"});
            }
            
        }
    });
    
}//end of creating user function


/**
 * Function is purpose is to grab user information for login from get call towards endpoint /userlogin
 * @param {*} req 
 * @param {*} res 
 */
function getUser(req,res){
    console.log("USERNAME: " + req.query.user);
    console.log("PASSWORD: " + req.query.password);

    var conn = mysql.createConnection( {
        host: "127.0.0.1",
        user: "nguyenrr",
        password: "$311ry559",
        database: "nguyen_tasks"
    });
    
    conn.connect(function(err){
        if(err) throw err;
        console.log("MYSQL CONNECTED.")
    })
    
    //SQL query
    let sql = "select user_id from users where user_name = " + '"'+ req.query.user + '"' + " AND " + "password = " + '"' + req.query.password + '"';
    
    //Running query
    console.log("QUERY: " + sql);

    conn.query(sql, function(err, rows){
        if(err){
            console.log("NOT FOUND");
            res.send("empty");
        }else{
            res.send(JSON.stringify(rows)); 
        }
          
    })
}


function grabName(req, res){
    //Grab users name from user_id
    let userID = req.query.user_num;

    var conn = mysql.createConnection({
        host: "127.0.0.1",
        user: "nguyenrr",
        password: "$311ry559",
        database: "nguyen_tasks"
    });

    conn.connect(function(err){
        if(err) throw err;
        console.log("MYSQL CONNECTED.")
    });

    let sql = "select name from users where user_id = " + '"' + userID + '"';

    conn.query(sql, function(err, result){
        if(err){
            console.log("NO USERS TO GRAB NAME");
        }else{
            res.send(JSON.stringify(result));
        }
    });
}

function grabAllTasks(req, res){
    let clientID = req.query.user_num;

    console.log("CLIENT ID: " + clientID);

    var conn = mysql.createConnection( {
        host: "127.0.0.1",
        user: "nguyenrr",
        password: "$311ry559",
        database: "nguyen_tasks"
    });
    
    conn.connect(function(err){
        if(err) throw err;
        console.log("MYSQL CONNECTED.")
    })
    let sql = "SELECT name, user_task, due_date, completed FROM users INNER JOIN tasks ON users.user_id = tasks.user_id where tasks.user_id=" + '"' + clientID + '"';

    conn.query(sql, function(err, result){
        if(err){
            console.log("INNER JOIN DIDN'T WORK");
        }else{
            res.send(JSON.stringify(result));
        }
    })
 
}

function createTask(req, res){
    let userID = req.query.user_id;
    let taskName = req.query.taskname;
    let dueDate = req.query.date;

    var conn = mysql.createConnection({
        host: "127.0.0.1",
        user: "nguyenrr",
        password: "$311ry559",
        database: "nguyen_tasks"
    });

    conn.connect(function(err){
        if(err) throw err;
        console.log("MYSQL CONNECTED.")
    });

    let sql = "insert into tasks (user_id, user_task, completed, due_date) VALUES (" + userID + ",'" + taskName + "'," + "false" + ",'" + dueDate + "')";

    conn.query(sql, function(err, result){
        if(err){
            console.log("FAILED TO ADD TASK")
        }else{
            res.send(JSON.stringify({message: "Task added"}));
        }
    })


}

function completeTask(req, res){
    let userID = req.query.user_id;
    let taskName = req.query.userTask;

    var conn = mysql.createConnection( {
        host: "127.0.0.1",
        user: "nguyenrr",
        password: "$311ry559",
        database: "nguyen_tasks"
    });
    
    conn.connect(function(err){
        if(err) throw err;
        console.log("MYSQL CONNECTED for complete Task.");
    });

    let sql = "update tasks set completed = true where user_id = " + userID + " AND user_task = " + '"' + taskName + '"';

    conn.query(sql, function(err, result){
        if(err){
            console.log("Couldn't update table for task completion");
        }else{
            res.send(JSON.stringify({message: "task has been updated"}));
        }
    });
}

function deleteTask(req, res){
    let userID = req.query.user_id;
    let taskName = req.query.userTask;

    var conn = mysql.createConnection( {
        host: "127.0.0.1",
        user: "nguyenrr",
        password: "$311ry559",
        database: "nguyen_tasks"
    });
    
    conn.connect(function(err){
        if(err) throw err;
        console.log("MYSQL connected for deleteTask Function");
    });

    let sql = "delete from tasks where user_id = " + '"' + userID + '" AND user_task = ' + '"' + taskName + '"';
    
    conn.query(sql, function(err){
        if(err){
            console.log("FAILED TO DELETE FROM DELETE FUNCTION");
        }else{
            res.send(JSON.stringify({message: "deleted"}));
        }
    })

}


 app.listen(3132);