<template>
     <div class="signup mt-5 pt-5">
        <div class="py-5 my-4">
            <h4 class="header">Thanks&#33; You’re almost there.</h4>
            <img class="py-4" src="../assets/emailsent.svg" width="150">
            <p class="desc">We’ve sent a confirmation email to <br> <b class="bold-color">{{getUser.email}}</b> </p>
       </div>

        <div class="onboard-footer text-center">
            <p>Didn’t receive an email? <router-link to="/verification_resend" class="signin" >Resend verification</router-link></p>
        </div>
    </div>
</template>

<script>
export default {
    name: "verifcation",
    computed: {
        getUser(){
            return this.$store.getters.getUser;
        },
        getToken(){
            return this.$store.getters.getToken;
        }
    },
    methods: {
        sendEmailVerification(){
            this.$http.post(this.getUser._links.sendConfirmation.href).then(res=>{
                if(res.status === 200){
                    return res.status
                }
            })
        }
    },
    mounted(){
        this.sendEmailVerification();
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

a.signin{
    text-decoration: none;
    color: #A93B3F;
    text-align: right !important;
}
b.bold-color{
    font-weight: 700;
}

.onboard-footer{
    position: absolute;
    bottom: 0;
    left: 12%;
}
</style>