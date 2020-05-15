<template>
    <div class="passwd-email py-5">
        <div class="">
            <img src="../assets/ashesi-logo-search.png" width="100">
            <h4 class="header mb-3 pt-4">Resend Verification</h4>
            <p class="desc">Enter your <b class="bold-color">Ashesi email address</b> below and we’ll <br> send you a new verification email </p>
        </div>
        <form id="passwd-email-form" v-on:submit.prevent="submit" class="pb-5 mb-5">
            <div class="row">
                 <div class="col-12 form-group">
                    <label class="col-form-label col-form-label-lg">Email<span class="text-danger">*</span></label>
                    <input type="email" v-model.trim="email" :class="{'is-invalid': $v.email.$error, 'is-valid':!$v.email.$invalid}" class="form-control form-control-lg">
                    <div v-if="!$v.email.required" class="invalid-feedback">The email field is required.</div>
                    <div v-if="!$v.email.email" class="invalid-feedback">The email is not valid.</div>
                </div>
                 <div class="col-12 form-group py-4">
                   <button class="btn btn-wine btn-lg col-12">Resend</button>
                </div>
            </div>
        </form>
    </div>
</template>

<script>
import { required, email } from 'vuelidate/lib/validators'
export default {
    name: "PasswdReset",
    data: function(){
        return {
            email: ''
        }
    },
    validations: {
        email: {required, email}
    },

    methods: {

        submit: function(){
            this.$v.$touch();
            if (this.$v.$pendding || this.$v.$error) return; 

            alert('Data submit');
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

b.bold-color{
    font-weight: 700;
}

a.signin{
    text-decoration: none;
    color: #A93B3F;
    text-align: right !important;
}
</style>