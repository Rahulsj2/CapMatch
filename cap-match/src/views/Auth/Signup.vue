<template>
    <div class="signup text-left" v-loading.fullscreen.lock="submitting">
        <img src="../../assets/ashesi-logo-search.png" width="100">
        <h4 class="header mb-3 pt-4">Fill in some basic information</h4>
        <p class="desc">See how CapMatch can help you continuously improve your <br>cyber security rating, detect exposures and control risk.</p>
        <div v-if="userExist" class="alert alert-danger" role="alert" >
            The user already exists
        </div>
        <form id="signup-form" v-on:submit.prevent="submit">
            <div class="row">
                <div class="col-12 form-group">
                    <label class="col-form-label col-form-label-lg">First name<span class="text-danger">*</span></label>
                    <input type="text" v-model.trim="$v.firstname.$model" :class="{'is-invalid': $v.firstname.$error, 'is-valid':!$v.firstname.$invalid}" class="form-control form-control-lg" autofocus>
                    <!-- <div v-if="!$v.firstname.required" class="invalid-feedback">The first name field is required.</div> -->
                </div>
                 <div class="col-12 form-group">
                    <label class="col-form-label col-form-label-lg">Last name<span class="text-danger">*</span></label>
                    <input type="text" v-model.trim="lastname" :class="{'is-invalid': $v.lastname.$error, 'is-valid':!$v.lastname.$invalid}" class="form-control form-control-lg">
                    <!-- <div v-if="!$v.lastname.required" class="invalid-feedback">The last name field is required.</div> -->
                </div>
                 <div class="col-12 form-group">
                    <label class="col-form-label col-form-label-lg">Email<span class="text-danger">*</span></label>
                    <input type="email" v-model.trim="email" :class="{'is-invalid': $v.email.$error, 'is-valid':!$v.email.$invalid}" class="form-control form-control-lg">
                    <!-- <div v-if="!$v.email.required" class="invalid-feedback">The email field is required.</div> -->
                    <!-- <div v-if="!$v.email.email" class="invalid-feedback">The email is not valid.</div> -->
                </div>
                 <div class="col-12 form-group">
                    <label class="col-form-label col-form-label-lg">Password<span class="text-danger">*</span></label> 
                    <input type="password" v-model.trim="password" :class="{'is-invalid': $v.password.$error, 'is-valid':!$v.password.$invalid}" class="form-control form-control-lg">  
                    <!-- <div v-if="!$v.password.required" class="invalid-feedback">The password field is required.</div>   -->
                    <!-- <div v-if="!$v.password.minLength" class="invalid-feedback">You must have at least {{$v.password.$params.minLength.min}} characters.</div>                 -->
                </div>
                 <div class="col-12 form-group py-4">
                   <button class="btn btn-wine btn-lg col-12" v-on:click="register()">Continue<i class="arrow fa fa-arrow-right mx-3"></i></button>
                </div>
            </div>
        </form>
        <div class="onboard-footer text-center">
            <p>Already have an account? <router-link to="/login" class="signin">Sign in</router-link></p>
        </div>
    </div>
</template>

<script>
import { mapGetters } from 'vuex';
import { required, email, minLength } from 'vuelidate/lib/validators'
export default {
    name: "Signup",
    data: function(){
        return {
            userExist: false,
            firstname: '',
            lastname: '',
            email: '',
            password: '',
            userType: '',
            major: '',
            department: '',
            submitting: false
        }
    },
    validations: {
        firstname: {required},
        lastname: {required},
        email: {required, email},
        password: {required, minLength: minLength(8)}
    },
    computed: {
        ...mapGetters(['getMajor']),
    },
    methods: {
        register(){
            // if (this.submit()){
                this.submitting = true;
                this.$store.dispatch('REGISTER',{
                    firstname: this.firstname,
                    lastname: this.lastname,
                    email: this.email,
                    password: this.password,
                    // major: getMajor
                    // user: this.userType
                })
                .then( success => {
                    // console.log(success)
                    if(success)
                    this.$router.push('/interests')
                    this.submitting = false
                })
                .catch ( error => {
                    // this.userExist = true;
                    this.submitting = false
                    return error
                })
        },
        submit(){
            this.$v.$touch();
            if (this.$v.$pendding || this.$v.$error) return; 

            // alert('Data submit');
        },
        
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

a.signin{
    text-decoration: none;
    color: #A93B3F;
    text-align: right !important;
}
</style>