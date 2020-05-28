<template>
  <div class="signup py-2 text-left"  v-loading.fullscreen.lock="submitting">
    <img src="../assets/ashesi-logo-search.png" width="100" />
    <h4 class="header mb-3 pt-4">What are your interests?</h4>
    <p class="desc">Pick all interests that apply</p>
    <div class="row h-100 my-4" v-if="interests.length > 0">
      <div
        v-for="interest in interests"
        :interest="interest"
        :key="interest.interestId"
        class="mx-2 my-auto"
      >

        <div class="mx-auto">
          <button v-on:click.prevent="toggleActive(interest)" :class="{active:activeInterests.includes(interest)}" class="btn btn-select my-3">{{interest.name}}</button>
        </div>
      </div>
    </div>
    <div class="col-12 form-group py-4">
      <button v-on:click.prevent="submit()" class="btn btn-wine btn-lg col-12">
        Continue
        <i class="arrow fa fa-arrow-right mx-3"></i>
      </button>
    </div>
  </div>
</template>

<script>
export default {
  name: "Interest",
  data: function() {
    return {
      submitting:false,
      interests: [],
      activeInterests: []
    };
  },
  created(){
      this.getInterests()
      },
  methods: {
    submit() {
      this.submitting = true;
      this.$store
        .dispatch("SENDINTERESTS", {
          interests: this.activeInterests
        })
        .then(success => {
          // console.log(success)
          if (success)
          this.$router.push("/sdgs");
          this.submitting = false;
        })
        .catch(error => {
          this.submitting = false;
          return error
        });
    },
    getInterests() {
      // console.log('Getting Interests')
        this.$http.get('/interests').then(res=>{
          this.interests = res.data._embedded.interests;
        });
    },
    addActiveInterest(interest){
      this.activeInterests.push( interest);
    },
    removeActiveInterest(index){
      this.activeInterests.splice(index,1)
    },
    toggleActive(interest){
      
      if(this.activeInterests.includes(interest)){
        const position = this.activeInterests.indexOf(interest);
        this.removeActiveInterest(position);
      }
      else 
        this.addActiveInterest(interest)
    }

  },
  watch: {
      $route: 'getInterests'
      }
};
</script>

<style>
h4.header {
  font-size: 32px;
  font-weight: 200;
}

p.desc {
  font-size: 16px;
  font-weight: 200;
}

.btn-select {
  color: #707070;
  font-size: 16px;
  font-weight: 400;
  border-color: #dedddd;
  /* padding: 10px 20px; */
  border-radius: 25px;
  display: inline-block;
  text-align: center;

}

.btn-select:hover {
  color: #a93b3f;
  border-color: #a93b3f;
  background-color: rgba(169, 59, 63, 0.1);
}

.active{
    color: #a93b3f;
    border-color: #a93b3f;
    background-color: rgba(169, 59, 63, 0.1);
}

.btn-wine {
  background: #a93b3f;
  color: #fff;
  font-size: 16px;
  font-weight: 400;
}

.btn-wine:hover {
  color: #fff;
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