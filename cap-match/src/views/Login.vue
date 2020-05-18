<template>
    <div class="signup py-2 text-left">
        <img src="../assets/ashesi-logo-search.png" width="100">
        <h4 class="header mb-3 pt-4">Sign in</h4>
        <p class="desc">See how CapMatch can help you continuously improve your <br>cyber security rating, detect exposures and control risk.</p>
        <form id="signup-form" v-on:submit.prevent="submit" class="pb-5 mb-5">
            <div class="row">
                 <div class="col-12 form-group">
                    <label class="col-form-label col-form-label-lg">Email<span class="text-danger">*</span></label>
                    <input type="email" v-model.trim="email"  class="form-control form-control-lg">
                </div>
                 <div class="col-12 form-group">
                    <label class="col-form-label col-form-label-lg">Password<span class="text-danger">*</span></label> 
                    <input type="password" v-model.trim="password" class="form-control form-control-lg"> 
                    <div class="text-right pt-2"><router-link to="/password_reset">Forgot password?</router-link></div> 
                </div>
                 <div class="col-12 form-group py-4">
                   <button v-on:click="signin" class="btn btn-wine btn-lg col-12">Sign in</button>
                </div>
            </div>
        </form>
        <div class="onboard-footer text-center pt-5">
            <p class="">Don&#39;t have an account? <router-link to="/signup" class="signup" href="#">Sign up</router-link></p>
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
    },
    methods: {
        signin: function(){
            this.$http.post('')
        }
    }

}
</script>

<style>

.form-control.is-invalid{
    border-color: #A93B3F;
    background-color: rgba(169, 59, 63, 0.10);
}

.form-control.is-invalid:focus{
    border-color: #A93B3F;
    box-shadow: 0 2px 4px 0.1rem rgba(78, 78, 78, 0.15);
}
.form-control.is-valid{
    border-color: #A93B3F;
}

.form-control.is-valid:focus{
    border-color: #A93B3F;
    box-shadow: 0 2px 4px 0.1rem rgba(78, 78, 78, 0.15);
}

#signup-form a{
    color: #A3A3A3;
    text-decoration: underline;
}

#signup-form a:hover{
    color: rgb(122, 122, 122);
}

h4.header{
    font-size: 32px;
    font-weight: 200;
}

p.desc{
    font-size: 16px;
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