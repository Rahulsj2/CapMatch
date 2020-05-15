<template>
    <div class="container-fluid py-5">
        <div class="row py-5">
            <div class="col-md-5 col-lg-5 d-flex justify-content-center align-items-center py-5">
                <div class="col-md-10 col-lg-10 mx-auto text-left align-items-start">      
                    <h4 class="header">Oops! We’ve got nothing here</h4>
                    <p class="desc">It seems you’ve hit a dead end. Don’t worry. <br>We’ll take you back. </p>
                   <button class="btn btn-wine btn-lg col-4"><i class="arrow fa fa-arrow-left mx-3"></i>Go back</button>
                </div>
                
            </div>
            <div class="col-md-7 col-lg-7 d-flex justify-content-center align-items-center ">
                <div class="col-md-10 col-lg-10 mx-auto text-left py-5 ">
                     <img class="py-4" src="../assets/404.svg" width="700">
                </div>
            </div>
        </div>
    </div>
</template>

<style>
h4.header{
    font-size: 32px;
    font-weight: 400;
}

p.desc{
    font-size: 24px;
    font-weight: 200;
}

.btn-wine{
    background: #A93B3F;
    color: #fff;
    font-size: 16px;
    font-weight: 400;
}

.btn-wine:hover{
    color: #fff;
}

</style>