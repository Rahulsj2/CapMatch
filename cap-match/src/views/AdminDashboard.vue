<template>
    <div class="container">
        <h2>Hello {{getUser.firstname}}</h2>
        <p>
            Get Started on your capstone journey
        </p>
        <div class="row mt-4 pt-5">
            <div class="col-sm-6">
                <button v-on:click.prevent="match"  class="btn btn-wine col-6 my-4">Initiate Matching</button>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: "AdminDashboard",
    computed: {
        getUser(){
            return this.$store.getters.getUser;
        },
        // getToken(){
        //     return this.$store.getters.getToken;
        // }
    },
    methods: {
        match(){
            console.log('Yawa paaaii....')
            console.log(this.getUser._links.initiateMatching.href)
            this.$http.post(this.getUser._links.initiateMatching.href).then(res=>{
                if(res.status === 200){
                    return res.status
                }
            })
        }
    }
}
</script>

<style>
.btn-wine{
    background: #A93B3F;
    color: #fff;
    font-size: 16px;
    font-weight: 400;
}

.btn-wine:hover{
    color: #fff;
}
</style>

