<template>
    <div class="signup py-2 text-left">
        <img src="../assets/ashesi-logo-search.png" width="100">
        <h4 class="header mb-3 pt-4">Create an account</h4>
        <p class="desc">See how CapMatch can help you continuously improve your <br>cyber security rating, detect exposures and control risk.</p>
        <div class="py-5">
            <button class="btn btn-wine btn-lg col-11 mb-3"><i class="icon fa fa-user-graduate mr-5"></i>STUDENT<i class="icon fa fa-arrow-right ml-5"></i></button>
            <button class="btn btn-wine btn-lg col-11 my-3"><i class="icon fa fa-chalkboard-teacher mr-5"></i>FACULTY<i class="icon fa fa-arrow-right ml-5"></i></button>
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

.icon{
    font-size: 24px;
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