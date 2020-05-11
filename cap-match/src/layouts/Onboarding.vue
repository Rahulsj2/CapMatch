<template>
    <div class="container-fluid">
        <div class="row no-gutters">
            <div class="col-md-8 col-lg-6">
                <p class="py-3 px-5">Already have an account? <a class="signin" href="#">Sign in</a> </p>
                <div class="d-flex align-items-center py-3">
                    <div class="container">
                        <div class="col-md-9 col-lg-8 mx-auto section">
                            
                            <!-- <h3 class="">Welcome please login to your account!</h3> -->
                            
                        </div>
                    </div>
                </div>
            </div>
            <div class="side-bg d-none d-md-flex col-lg-6 py-5">
                <div class="col-md-10 col-lg-9 mx-auto text-left py-5 ">
                    <h3 class="info mb-3 pt-5">Start your<br> <span class="cap">Capstone</span> Journey</h3>
                    <p class="info-p">Search for a supervisor in minutes.</p>
                    <img class="gradcap" alt="Graduation cap" src="../assets/gradcap.svg" width="600"> 
                </div>  
            </div>
            
        </div>
    </div>
</template>


<style>

.side-bg{
    background: #FAFAFA;
}

.cap {
    color: #A93B3F;
}

.info {
    font-size: 40px;
}

.info-p {
    font-size: 24px;
    font-weight: 200;
}

.gradcap{
    margin-left: 180px !important;
    margin-top: 130px !important;
    /* margin-right: -20px !important; */
    /* position: inherit; */
}
.container-fluid{
    height: 100%;
    overflow-x: hidden;
    overflow-y: hidden;
}

a.signin{
    text-decoration: none;
    color: #A93B3F;
    text-align: right !important;
}
p.py-3{
    text-align: right;
}

</style>
