<template>
     <div class="reset text-left">
        <img src="../assets/ashesi-logo-search.png" width="100">
        <h4 class="header mb-3 pt-5">Reset your password</h4>
        <form id="signup-form"  class="mb-5 pb-5">
            <div class="row">
                <div class="col-12 form-group">
                    <label class="col-form-label col-form-label-lg">Old password<span class="text-danger">*</span></label>
                    <input type="password" v-model.trim="$v.oldpassword.$model" :class="{'is-invalid': $v.oldpassword.$error, 'is-valid':!$v.oldpassword.$invalid}" class="form-control form-control-lg" autofocus>
                    <!-- <div v-if="!$v.firstname.required" class="invalid-feedback">The first name field is required.</div> -->
                </div>
                 <div class="col-12 form-group">
                    <label class="col-form-label col-form-label-lg">New password<span class="text-danger">*</span></label>
                    <input type="password" v-model.trim="$v.newpassword.$model" :class="{'is-invalid': $v.newpassword.$error, 'is-valid':!$v.newpassword.$invalid}" class="form-control form-control-lg">
                    <!-- <div v-if="!$v.lastname.required" class="invalid-feedback">The last name field is required.</div> -->
                </div>
                 <div class="col-12 form-group">
                    <label class="col-form-label col-form-label-lg">Confrim password<span class="text-danger">*</span></label>
                    <input type="password"  class="form-control form-control-lg" v-model.trim="$v.repeatpassword.$model" :class="{
                        'is-invalid': $v.repeatpassword.$error, 'is-valid': (newpassword != '') ?
                        !$v.password.$invalid : '' }">
                    <!-- <div v-if="!$v.email.required" class="invalid-feedback">The email field is required.</div> -->
                    <!-- <div v-if="!$v.email.email" class="invalid-feedback">The email is not valid.</div> -->
                </div>
                 <div class="col-12 form-group py-4">
                   <button class="btn btn-wine btn-lg col-12">Reset Password</button>
                </div>
            </div>
        </form>
        <!-- <form id="psswdreset-form" class="mb-5 pb-5">
            <div class="row">
                <div class="col-12 form-group">
                    <label class="col-form-label col-form-label-lg">Old password<span class="text-danger">*</span></label> 
                    <input type="password" v-model.trim="$v.oldpassword.$model" :class="{'is-invalid': $v.oldpassword.$error, 'is-valid':!$v.oldpassword.$invalid}" class="form-control form-control-lg">
                    <div class="invalid-feedback">
                        <span v-if="!$v.oldpassword.required">Your old password is required.</span>
                    </div>                
                </div>
                <div class="col-12 form-group">
                    <label class="col-form-label col-form-label-lg">New password<span class="text-danger">*</span></label> 
                     <input type="password" v-model.trim="$v.newpassword.$model" :class="{'is-invalid': $v.newpassword.$error, 'is-valid':!$v.newpassword.$invalid}" class="form-control form-control-lg">
                     <div class="invalid-feedback">
                        <span v-if="!$v.newpassword.required">New password field is required.</span>
                        <span v-if="!$v.newpassword.minLength">You must have at least {{$v.newpassword.$params.minLength.min}} characters.</span>
                    </div>               
                </div>
                <div class="col-12 form-group">
                    <label class="col-form-label col-form-label-lg">Confirm password<span class="text-danger">*</span></label> 
                    <input type="password" v-model.trim="$v.repeatpassword.$model" :class="{'is-invalid': $v.repeatpassword.$error, 'is-valid': (newpassword != '') ? !$v.password.$invalid : '' }" class="form-control form-control-lg">
                    <div class="invalid-feedback">
                        <span v-if="!$v.repeatpassword.sameAsPassword">Passwords must be a match.</span>
                    </div>                
                </div>
                 <div class="col-12 form-group py-4">
                   <button class="btn btn-wine btn-lg col-12">Reset Password</button>
                </div>
            </div>
        </form> -->
    </div>
</template> 

<script>
import { required, minLength, sameAs  } from 'vuelidate/lib/validators'
export default {
    name: "PasswdResetForm",
     data: function(){
        return {
            oldpassword: '',
            newpassword: '',
            repeatpassword: ''
        }
    },
    validations: {
        oldpassword: {required},
        newpassword: {required, minLength: minLength(8)},
        repeatpassword: { sameAsPassword: sameAs('newpassword')}
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


</style>