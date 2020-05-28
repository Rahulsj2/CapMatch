<template>
     <div class="signup mt-5 pt-5">
    </div>
</template>

<script>
export default {
    name: "confirmaccount",
    data (){
        return {
            confirmCode: ""
        }
    },
    methods: {
        getCode(){
            this.confirmCode = this.$route.query.confirmCode
        },
        sendCode(){
            this.$store.dispatch('CONFIRMUSER', {
                confirmCode: this.confirmCode
            })
            .then( success => {
                console.log(success)
                if(success)
                this.$router.push('/login')
                // return success;
            })
            .catch( error =>{
                return error;
            })
        }
    },
    mounted(){
        this.getCode();
        // console.log(this.confirmCode);
        this.sendCode();
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