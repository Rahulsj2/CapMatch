<template>
    <div class="login">
        <img src="../assets/ashesi-logo-search.png" width="100">
        <h4 class="header mb-3 pt-4">Sign in</h4>
        <p class="desc">See how CapMatch can help you continuously improve your <br>cyber security rating, detect exposures and control risk.</p>
        <!-- <form id="signup-form" v-on:submit.prevent="submit"> -->
        <form id="login-form" class="mb-5 pb-5">
            <div class="row">
                 <div class="col-12 form-group">
                    <label class="col-form-label col-form-label-lg">Email<span class="text-danger">*</span></label>
                    <input type="email" v-model.trim="email" class="form-control form-control-lg">
                </div>
                 <div class="col-12 form-group">
                    <label class="col-form-label col-form-label-lg">Password<span class="text-danger">*</span></label> 
                    <input type="password" v-model.trim="password" class="form-control form-control-lg"> 
                    <div class="text-right pt-1"><a  href="#">Forgot password?</a> </div>
                    
                </div>
                 <div class="col-12 form-group py-4">
                   <button class="btn btn-wine btn-lg col-12">Sign in</button>
                </div>
            </div>
        </form>
        <div class="onboard-footer text-center pt-5">
            <p>Don&#39;t have an account? <a class="signin" href="#">Sign up</a></p>
        </div>
    </div>
</template> 

<script>
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
#login-form a{
    text-decoration: underline;
    color: #A3A3A3;
    font-size: 14px;
}

#login-form a:hover{
    color: rgb(143, 142, 142);
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

.col-form-label-lg{
    font-size: 14px;
    font-weight: 200;
}

a.signin{
    text-decoration: none;
    color: #A93B3F;
    text-align: right !important;
}
</style>