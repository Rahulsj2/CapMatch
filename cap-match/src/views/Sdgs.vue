<template>
    <div class="signup py- text-left"  v-loading.fullscreen.lock="submitting">
        <img src="../assets/ashesi-logo-search.png" width="100">
        <h4 class="header mb-2 pt-3">What SDGs are you Sdged in?</h4>
         <p class="desc">Pick all sdgs that apply</p>
        <div class="row  my-2 ">
            <div 
              v-for="sdg in sdgs"
              :sdg="sdg"
              :key="sdg.number"
              class="mx-2 my-auto"
            >
                <button  v-on:click.prevent="toggleActive(sdg)" :class="{active:activeSdgs.includes(sdg)}" class="card" style="width: 6rem;">
                    <div class="card-body text-center">
                        <h5 class="card-title">{{sdg.number}}</h5>
                        <p class="card-text">{{sdg.name}}</p>
                    </div>
                </button>
            </div>

            <div class="col-lg-9 my-1 ">
            </div>
            <button class="btn btn-wine btn-lg col-4 ml-3 mr-5 mt-3"><i class="arrow fa fa-arrow-left mx-3"></i>Previous</button>
            <button class="btn btn-wine btn-lg col-4 ml-5 pl-4 mt-3" v-on:click.prevent="submit()">Continue<i class="arrow fa fa-arrow-right mx-3"></i></button>
        </div>
       

    </div>
</template>

<script>
export default {
  name: "Sdgs",
  data: function() {
    return {
      submitting: false,
      sdgs: [],
      activeSdgs: []
    };
  },
  created(){
      this.getSdgs()
  },
  methods: {
    submit() {
      this.submitting = true;
      this.$store
        .dispatch("SENDSDGS", {
          sdgs: this.activeSdgs
        })
        .then( success => {
          // console.log(success)
          if(success)
          this.$router.push("/verification");
          this.submitting = false;
        })
        .catch(error => {
          this.submitting = false;
          return error;
        });
    },
    getSdgs() {
    //   console.log('Getting sdgs')
        this.$http.get('/sDGs').then(res=>{
          this.sdgs = res.data._embedded.sDGs;
        });
    },
    addActiveSdg(sdg){
      this.activeSdgs.push(sdg);
    },
    removeActiveSdg(index){
      this.activeSdgs.splice(index,1)
    },
    toggleActive(sdg){
      if (this.activeSdgs.includes(sdg)){
        const position = this.activeSdgs.indexOf(sdg)
        this.removeActiveSdg(position);
      }
      else{
        this.addActiveSdg(sdg);
      } 
    }

  },
  watch: {
      $route: 'getSdgs'
      }
};
</script>
<style>
.card-title{
    font-size: 24px;
    font-weight: 700;
}
.card-text{
    font-size: 8px;
    font-weight: 400;
}
h4.header{
    font-size: 30px;
    font-weight: 200;
}

.active{
    color: #a93b3f;
    border-color: #a93b3f;
    background-color: rgba(169, 59, 63, 0.1);
}

p.desc{
    font-size: 16px;
    font-weight: 200;
}

.card:hover{
    color: #A93B3F;
    border-color: #A93B3F;
    background-color: rgba(169, 59, 63, 0.10);
}

.btn-wine{
    color: #707070;
    font-size: 16px;
    font-weight: 400;
}

.btn-wine:hover{
    color: #fff;
    background: #A93B3F;
}

</style>