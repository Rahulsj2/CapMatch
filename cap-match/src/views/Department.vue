<template>
    <div class="signup py-2 text-left">
        <img src="../assets/ashesi-logo-search.png" width="100">
        <h4 class="header mb-3 pt-4">What is your dept?</h4>
        <div class="row h-100  my-4 ">
            <button v-for="(department,index) in departments" :key="department.departmentCode"  v-on:click.prevent="addDepartment(index)"  class="btn btn-user btn-lg col-6 major my-2  text-left">
                <div class="row h-100">
                    <div class="col-lg-9 ">
                        <div class="mx-auto about">
                            <h1 class="mb-4 card-title">{{(department.departmentCode)[0]}}</h1>
                        </div>
                    </div>
                    <div class="col-lg-3  my-1">     
                    <i class="icon fa fa-arrow-right"></i>
                    </div>    
                </div>
                <p class="dept-name">{{department.name}}</p>
            </button>

        </div>
                
        <div class="onboard-footer text-center">
            <p class="">Already have an account? <router-link to="/login" class="signup" href="#">Sign in</router-link></p>
        </div>
    </div>
</template>

<script>
// import axios from 'axios';
// import { serverBaseURL } from '../variables.js'
export default {
    name: "Department",
    data(){
        return {
            departments: []
        }
    },
    methods: {
        addDepartment(index){
            this.submitting = true
            this.$store.commit("changeDepartment", this.departments[index]);
            this.$router.push('/signup');
            this.submitting = false
        },
        getDepartment(){
            this.$http.get('/departments').then(res=>{
                if (res.status === 200){
                    this.departments = res.data._embedded.departments;
                }
            })
        }
    },
    mounted(){
       this.getDepartment();
    }

}
</script>

<style>
.card-title{
    font-size: 24px;
    font-weight: 700;
}
.dept-name{
    font-size: 16px;
    font-weight: 700;
}
.card:hover{
    color: #A93B3F;
    border-color: #A93B3F;
    background-color: rgba(169, 59, 63, 0.10);
}

h4.header{
    font-size: 32px;
    font-weight: 200;
}
.btn-user{
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

p.desc{
    font-size: 16px;
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