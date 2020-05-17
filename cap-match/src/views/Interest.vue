<template>
    <div class="signup py-2 text-left">
        <img src="../assets/ashesi-logo-search.png" width="100">
        <h4 class="header mb-3 pt-4">What are your interests?</h4>
         <p class="desc">Pick all interests that apply</p>
        <div class="row h-100  my-4 ">
            <div class="col-lg-4 my-auto ">
                <div class="mx-auto">
                   <button class="btn btn-select btn-lg col-11 my-3">Finance</button>
                </div>
            </div>
            <div class="col-lg-4 my-auto ">
                <div class="mx-auto">
                   <button class="btn btn-select btn-lg col-11 my-3">Education</button>
                </div>
            </div>
           <div class="col-lg-4 my-auto ">
                <div class="mx-auto">
                   <button class="btn btn-select btn-lg col-11 my-3">Technology</button>
                </div>
            </div>

            <div class="col-lg-4 my-auto ">
                <div class="mx-auto">
                   <button class="btn btn-select btn-lg col-11 my-3">Agriculture</button>
                </div>
            </div>

            <div class="col-lg-6 my-auto ">
                <div class="mx-auto">
                   <button class="btn btn-select btn-lg col-11 my-3">Internet of Things</button>
                </div>
            </div>

            <div class="col-lg-6 my-auto ">
                <div class="mx-auto">
                   <button class="btn btn-select btn-lg col-11 my-3">Machine Learning</button>
                </div>
            </div>
            <div class="col-lg-6 my-auto ">
                <div class="mx-auto">
                   <button class="btn btn-select btn-lg col-11 my-3">Web Technologies</button>
                </div>
            </div>
             <div class="col-lg-4 my-auto ">
                <div class="mx-auto">
                   <button class="btn btn-select btn-lg col-11 my-3">Networks</button>
                </div>
            </div>
             <div class="col-lg-8 my-auto ">
                <div class="mx-auto">
                   <button class="btn btn-select btn-lg col-11 my-3">Mobile App Development</button>
                </div>
            </div>
             <div class="col-lg-4 my-auto ">
                <div class="mx-auto">
                   <button class="btn btn-select btn-lg col-11 my-3">Lifestyle</button>
                </div>
            </div>
            
             <div class="col-lg-8 my-auto ">
                <div class="mx-auto">
                   <button class="btn btn-select btn-lg col-11 my-3">Human Computer Interaction</button>
                </div>
            </div>
        </div>
        <div class="col-12 form-group py-4">
            <button class="btn btn-wine btn-lg col-12">Continue<i class="arrow fa fa-arrow-right mx-3"></i></button>
        </div>
       

<!-- 
        <div class="onboard-footer text-center pt-5">
            <p class="">Already have an account? <router-link to="/login" class="signup" href="#">Sign in</router-link></p>
        </div> -->
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


h4.header{
    font-size: 32px;
    font-weight: 200;
}


p.desc{
    font-size: 16px;
    font-weight: 200;
}

.btn-select{
    /* background: #A93B3F; */
    color: #707070;
    font-size: 16px;
    font-weight: 400;
    border-color: #DEDDDD;
    padding: 10px 20px;
    border-radius: 25px;
}

.btn-select:hover{
    color: #A93B3F;
    border-color: #A93B3F;
    background-color: rgba(169, 59, 63, 0.10);
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