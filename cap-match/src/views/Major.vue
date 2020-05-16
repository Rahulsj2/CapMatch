<template>
    <div class="signup py-2 text-left">
        <img src="../assets/ashesi-logo-search.png" width="100">
        <h4 class="header mb-3 pt-4">What is your major?</h4>
        <div class="row h-100  my-4 ">
            <div class="col-lg-4 my-auto ">
                <div class="mx-auto">
                    <div class="card" style="width: 14rem;">
                        <div class="card-body">
                            <div class="row h-100">
                                 <div class="col-lg-9 ">
                                    <div class="mx-auto about">
                                        <h1 class="mb-4 card-title">C</h1>
                                    </div>
                                </div>
                                <div class="col-lg-3  my-1">     
                                   <i class="icon fa fa-arrow-right"></i>
                                </div>    
                            </div>
                            <p class="major-name">Computer<br> Science</p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-lg-3 my-3 ">
                <div class="mx-auto">
                </div>
            </div>
           <div class="col-lg-4 my-auto ">
                <div class="mx-auto">
                    <div class="card" style="width: 14rem;">
                        <div class="card-body">
                            <div class="row h-100">
                                 <div class="col-lg-9 ">
                                    <div class="mx-auto about">
                                        <h1 class="mb-4 card-title">M</h1>
                                    </div>
                                </div>
                                <div class="col-lg-3  my-1">     
                                   <i class="icon fa fa-arrow-right"></i>
                                </div>    
                            </div>
                            <p class="major-name">Management <br> Information Systems</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row h-100  my-4">
            <div class="col-lg-4 my-auto ">
                <div class="mx-auto">
                    <div class="card" style="width: 14rem;">
                        <div class="card-body">
                            <div class="row h-100">
                                 <div class="col-lg-9 ">
                                    <div class="mx-auto about">
                                        <h1 class="mb-4 card-title">B</h1> 
                                    </div>
                                </div>
                                <div class="col-lg-3  my-1">     
                                   <i class="icon fa fa-arrow-right"></i>
                                </div>    
                            </div>
                            <p class="major-name">Business<br> Administration </p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-lg-3 my-3 ">
                <div class="mx-auto">
                </div>
            </div>
           <div class="col-lg-4 my-auto ">
                <div class="mx-auto">
                    <div class="card" style="width: 14rem;">
                        <div class="card-body">
                            <div class="row h-100">
                                 <div class="col-lg-9 ">
                                    <div class="mx-auto about">
                                        <h1 class="mb-4 card-title">E</h1>
                                    </div>
                                </div>
                                <div class="col-lg-3  my-1">     
                                   <i class="icon fa fa-arrow-right"></i>
                                </div>    
                            </div>
                            <p class="major-name">Electrical and <br> Electronic Engineering </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row h-100 my-4">
           <div class="col-lg-4 my-auto ">
                <div class="mx-auto">
                    <div class="card" style="width: 14rem;">
                        <div class="card-body">
                            <div class="row h-100">
                                 <div class="col-lg-9 ">
                                    <div class="mx-auto about">
                                        <h1 class="mb-4 card-title">M</h1>
                                    </div>
                                </div>
                                <div class="col-lg-3  my-1">     
                                   <i class="icon fa fa-arrow-right"></i>
                                </div>    
                            </div>
                            <p class="major-name">Mechanical <br> Engineering </p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-lg-3 my-3 ">
                <div class="mx-auto">
                </div>
            </div>
           <div class="col-lg-4 my-auto ">
                <div class="mx-auto">
                    <div class="card" style="width: 14rem;">
                        <div class="card-body">
                            <div class="row h-100">
                                 <div class="col-lg-9 ">
                                    <div class="mx-auto about">
                                        <h1 class="mb-4 card-title">C</h1>  
                                    </div>
                                </div>
                                <div class="col-lg-3  my-1">     
                                   <i class="icon fa fa-arrow-right"></i>
                                </div>    
                            </div>
                            <p class="major-name">Computer <br>Engineering </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
                


        <div class="onboard-footer text-center pt-5">
            <p class="">Don&#39;t have an account? <a class="signup" href="#">Sign up</a></p>
        </div>
    </div>
</template>

<script>
// import { required, email, minLength } from 'vuelidate/lib/validators'
export default {
    name: "LoginForm",
    data: function(){
        return {
            email: '',
            password: ''
        }
    }

}
</script>

<style>
.card-title{
    font-size: 24px;
    font-weight: 700;
}
.major-name{
    font-size: 16px;
    font-weight: 700;
}
.card:hover{
    color: #A93B3F;
    border-color: #A93B3F;
    background-color: rgba(169, 59, 63, 0.10);
}

h4.header{
    font-size: 32px;
    font-weight: 200;
}



.btn-lg {
    padding: 30px 20px;
}

p.desc{
    font-size: 16px;
    font-weight: 200;
}

.btn-wine{
    /* background: #A93B3F; */
    color: #707070;
    font-size: 16px;
    font-weight: 400;
    border-color: #DEDDDD;
}

.btn-wine:hover{
    color: #A93B3F;
    border-color: #A93B3F;
    background-color: rgba(169, 59, 63, 0.10);
}

.col-form-label-lg{
    font-size: 14px;
    font-weight: 200;
}

a.signup{
    text-decoration: none;
    color: #A93B3F;
    text-align: right !important;
}
.onboard-footer{
    position: absolute;
    bottom: 0;
    left: 20%;
}
</style>