<template>
  <div class="signup py-2 text-left">
    <img src="../assets/ashesi-logo-search.png" width="100" />
    <h4 class="header mb-3 pt-4">What is your major?</h4>
    <div class="row h-100  my-1">
      <button v-for="(major,index) in majors" :key="major.majorCode" v-on:click.prevent="addMajor(index)" class="btn btn-user btn-lg col-5 major my-2 mx-2 text-left">
        <div class="row h-100">
          <div class="col-lg-9">
            <div class="mx-auto about">
              <h1 class="mb-4 card-title">{{major.majorCode[0]}}</h1>
            </div>
          </div>
          <div class="col-lg-3">
            <i class="icon fa fa-arrow-right"></i>
          </div>
        </div>
        <p class="major-name">
          {{major.name}}
        </p>
      </button>
    </div>

    <div class="onboard-footer text-center">
      <p class="">
        Already have an account? <router-link to="/login" class="signup">Sign in</router-link>
      </p>
    </div>
  </div>
</template>

<script>
export default {
  name: "major",
  data: function(){
    return {
      majors: []
    }
  },
  methods: {
    addMajor(index){
        this.$store.commit("changeMajor", this.majors[index]);
        this.$router.push('/signup')
  
    },
    getMajors(){
      this.$http.get('/majors').then(res=>{
        if(res.status === 200){
          this.majors = res.data._embedded.majors;
        }
      })
    }
  },
  mounted(){
    this.getMajors();
  }
 
};
</script>

<style>
.card-title {
  font-size: 24px;
  font-weight: 700;
}
.major-name {
  font-size: 16px;
  font-weight: 700;
}
.card:hover {
  color: #a93b3f;
  border-color: #a93b3f;
  background-color: rgba(169, 59, 63, 0.1);
}

h4.header {
  font-size: 32px;
  font-weight: 200;
}

p.desc {
  font-size: 16px;
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

a.signup {
  text-decoration: none;
  color: #a93b3f;
  text-align: right !important;
}
.onboard-footer {
  position: absolute;
  bottom: 0;
  left: 20%;
}
</style>
