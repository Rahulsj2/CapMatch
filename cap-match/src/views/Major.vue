<template>
    <div class="signup py-2 text-left">
        <img src="../assets/ashesi-logo-search.png" width="100">
        <h4 class="header mb-3 pt-4">What is your major?</h4>
        <div class="row h-100  my-4 ">
            <router-link to="/signup" class="major my-2">
                <div class="col-lg-4 my-auto ">
                    <div class="mx-auto">
                        <div class="card" style="width: 14rem;">
                            <div class="card-body">
                                <div class="row h-100">
                                    <div class="col-lg-8 ">
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
            </router-link>
           <div class="col-lg-4 my-2">
                <router-link to="/signup" class="major">
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
                </router-link>
            </div>
            <router-link to="/signup" class="major my-2">
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
            </router-link>
            <div class="col-lg-4 my-2">
               <router-link to="/signup" class="major">
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
               </router-link>
            </div>
            <router-link to="/signup" class="major my-2">
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
            </router-link>
           <div class="col-lg-4 my-2">
               <router-link to="/signup" class="major">
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
                                <p class="major-name">Computer <br>  Engineering </p>
                            </div>
                        </div>
                    </div>
               </router-link>
            </div>
        </div>
                


        <div class="onboard-footer text-center">
            <p class="">Already have an account? <router-link to="/login" class="signup" >Sign in</router-link></p>
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

p.desc{
    font-size: 16px;
    font-weight: 200;
}

.major{
    color: #707070;
}

.major:hover{
    text-decoration: none;
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