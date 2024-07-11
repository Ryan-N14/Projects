/**
 * Ryan Nguyen
 * index.js
 * For the landing page to check user or sign up user
 */

$(main);


function main(){
    console.log("DOM ready")
    eventListners();
}

function eventListners(){
    $("#menu-container > button").click(changeTabs);
    $("#loginbtn").click(loadContent);
    $("#signup-btn").click(loadContent);
}

/**
 * to change the tabs between sign up and login
 */
function changeTabs(){
    if($(this).html() == "Sign up"){
        console.log("Signing up")
        $("#signuptab").attr("disabled", true);
        $("#logintab").attr("disabled", false);

        $("#signup-container").css("display", "block");
        $("#login-container").css("display", "none");

        $("#signuptab").toggleClass("active");
        $("#logintab").removeClass("active");
    }else{
        console.log("Logging in");

        $("#logintab").toggleClass("active");
        $("#signuptab").removeClass("active");

        $("#logintab").attr("disabled", true);
        $("#signuptab").attr("disabled", false);
        
        
        $("#login-container").css("display", "block");
        $("#signup-container").css("display", "none");
        
        
        $("#menu-container > button").css("background-color", "none");
    }
    
}

/**
 * Function to see if user is creating an account or logging in. First ajax call is to create user and second is to find if theres a user
 */
function loadContent(){

    if($(this).html() == "Create"){// SIGN UP SECTION
        console.log("CREATING USERS ACCOUNT...")
        
        //new account information
        let clientName = $("#signup-name-input").val();
        let clientUsers = $("#user-signup-input").val();
        let clientPassword = $("#password-signup-input").val();

        
        //AJAX CALL TO CREATE A NEW USER
        $.ajax({
            url:"http://augwebapps.com:3132/createuser?name=" + clientName + "&username=" + clientUsers + "&password=" + clientPassword,
            method: "GET"
        }).done(function(data){
            console.log("MYSQL response...");
            let temp = JSON.stringify(data);
            let result = JSON.parse(temp);

            console.log("RESPONSE : " + result.response);

          
            
            
            if(result.response === "User already exist"){
                console.log("Username or Password already has an account assiocated with it...")
                $("#signup-container > h4").css("display", "block");
            }else{
                let userID = JSON.parse(result)[0];
                console.log("SUCESSFULLY CREATED");
                console.log("Loading dashboard...");

                sessionStorage.setItem("user", userID.user_id);

                //Opening new window
                window.open("main.html", "_self" );
            }
        })



    }else{//ELSE STATEMENT IF USER IS LOGGING IN INSTEAD
        console.log("LOADING USERS PAGE");

        let clientUserName = $("#user-login-input").val();
        let clientPassword = $("#password-login-input").val();

        $.ajax({
            url:"http://augwebapps.com:3132/userlogin?user=" + clientUserName + "&password=" + clientPassword,
            method: "GET",
        }).done(function(data){
            try{
              console.log("MYSQL response...")
              $("#login-container > h4").css("display", "none");

              let temp = JSON.parse(data)[0];
              console.log("User_ID: " + temp.user_id); 
              sessionStorage.setItem("user", temp.user_id)

              //Opening new window
              window.open("main.html", "_self");
            }catch{
                //There's no user matching that information
                console.log("EMPTY");
                $("#login-container > h4").css("display", "block");
            }
        })
    }
}//end of load content function
