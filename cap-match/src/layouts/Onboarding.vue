<template>
    <div class="container-fluid">
        <div class="row">
            <div class="col-md-6 col-lg-6 d-flex justify-content-center align-items-center py-5">
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


</style>