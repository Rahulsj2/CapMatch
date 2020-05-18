<template>
    <div class="signup py-2 text-left">
        <img src="../assets/ashesi-logo-search.png" width="100">
        <h4 class="header mb-3 pt-4">Create an account</h4>
        <p class="desc">See how CapMatch can help you continuously improve your <br>cyber security rating, detect exposures and control risk.</p>
        <div class="py-5">
            <router-link to="/major" tag="button" v-on:click="addUserType" class="btn btn-user btn-lg col-11 mb-3"><i class="icon fa fa-user-graduate mr-5"></i>STUDENT<i class="icon fa fa-arrow-right ml-5"></i></router-link>
            <router-link to="/department" tag="button" v-on:click="addUserType"  class="btn btn-user btn-lg col-11 my-3"><i class="icon fa fa-chalkboard-teacher mr-5"></i>FACULTY<i class="icon fa fa-arrow-right ml-5"></i></router-link>
        </div>     
        
                


        <div class="onboard-footer text-center pt-5">
            <p class="">Already have an account? <router-link to="/login" class="signup" href="#">Sign in</router-link></p>
        </div>
    </div>
</template>

<script>

export default {
    name: "LoginForm",
    data: function(){
        return {
            userType: 'ddjdjdj'
        }
    },
    methods: {
        addUserType: function(){
            this.$http.post('https://capmatch-staging.cfapps.io/users', {
                userType: this.userType

            }).then(function(data){
                console.log(data);
            });
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


p.desc{
    font-size: 16px;
    font-weight: 200;
}

.btn-user{
    /* background: #A93B3F; */
    color: #707070;
    font-size: 16px;
    font-weight: 400;
    border-color: #DEDDDD;
    padding: 30px 20px;
}

.btn-user:hover{
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