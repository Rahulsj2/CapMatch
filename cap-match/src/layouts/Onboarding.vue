<template>
    <div class="container-fluid">
        <div class="row">
            <div class="col-md-6 col-lg-6 d-flex justify-content-center py-5">
                <div class="col-md-9 col-lg-8 mx-auto text-left">      
                    <slot/>
                </div>
            </div>
            <div class="col-md-6 col-lg-6 image-container d-none d-md-block image-container">
                <div class="col-md-10 col-lg-9 mx-auto text-left py-5 ">
                    <h3 class="info mb-3 pt-5">Start your<br> <span class="cap">Capstone</span> Journey</h3>
                    <p class="info-p">Search for a supervisor in minutes.</p>
                </div>
            </div>
        </div>
    </div>
</template>

<style>

.image-container{
    background: url("../assets/gradcap.svg") no-repeat;
    background-size: 600px;
    height: 100vh;
    background-position: 290px 440px;
    background-color: #fafafa;
}

.cap {
    color: #a93b3f;
}

.info {
    font-size: 40px;
}

.info-p {
    font-size: 24px;
    font-weight: 200;
}

textarea:focus,
input[type="text"]:focus,
input[type="password"]:focus,
input[type="datetime"]:focus,
input[type="datetime-local"]:focus,
input[type="date"]:focus,
input[type="month"]:focus,
input[type="time"]:focus,
input[type="week"]:focus,
input[type="number"]:focus,
input[type="email"]:focus,
input[type="url"]:focus,
input[type="search"]:focus,
input[type="tel"]:focus,
input[type="color"]:focus,
.uneditable-input:focus + label{   
  border-color: rgba(169, 59, 63, 1);
  box-shadow: 0 1px 1px rgba(0, 0, 0, 0.075) inset, 0 0 2px rgba(169, 59, 63, 1);
  outline: 0 none;
}

.btn.active.focus, .btn.active:focus, .btn.focus, .btn.focus:active, .btn:active:focus, .btn:focus {
    background-image: none;
    outline: 0 none;
    box-shadow: 0 1px 1px rgba(0, 0, 0, 0.075) inset, 0 0 2px rgba(169, 59, 63, 1);
}


</style>