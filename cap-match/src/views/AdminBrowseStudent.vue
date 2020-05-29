<template>
    <div class="container text-left">
        <h2>Student Profiles</h2>
        <div class="row mt-4 pt-4">
            <div class="col-sm-4 my-3" v-for="profile in profiles" :key="profile.id">
                <div class="card">
                    <div class="card-body">
                        <div class="row">
                            <div class="col-lg-10">
                                <router-link to="/profile" class="card-title">{{profile.firstname + " " + profile.lastname}}</router-link>
                            </div>
                            <div class="col-lg-2">
                                <i class="icon fa fa-check square-icon"></i>
                            </div>
                        </div>
                        <p class="card-text">{{profile.department}}</p>
                        <div class="row">
                            <div class="col-lg-3">
                                <span href="#" class="btn circle-icon"></span>
                            </div>
                            <div class="col-lg-8">
                                <div  class="btn btn-green mt-2">
                                    <!-- <i class="fas fa-align-left"></i> -->
                                    <span>Suggested</span>
                                </div>
                            </div>
                        </div>
                        
                    
                    </div>
                </div>
            </div>

        </div>
    </div>
</template>

<script>

export default {
    name: "Profiles",
    data: function(){
        return {
            profiles: [],
        }
    },
    computed: {
        getUser(){
            return this.$store.getters.getUser;
        }
    },
    methods: {
        getProfiles(){
            if(this.getUser.roles.includes("FACULTY")){
                this.$http.get(this.getUser._links.browseStudent.href).then(res =>{
                    if(res.status === 200){
                        this.profiles = res.data._embedded.students
                    }
                })
            }
        }

    },
    mounted(){
        this.getProfiles();
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
.card-title{
    color: #707070;
}
.card-title:hover{
    color: #707070;
    text-decoration: underline;
}
.btn-wine:hover{
    color: #fff;
}
.card-text {
    font-size: 14px;
    font-weight: 400;
}
.card-title{
    font-size: 18px;
    font-weight: 700;
}

.btn-green{
    color: #39903F;
  margin-left: 60%;
  background: rgba(59, 169, 66, 0.438);
}

.btn-green:hover{
    color: #39903F;
}

.circle-icon {
  background: none;
  text-align: center;
	border: 3px solid #eee;
	border-radius: 50%;
	box-sizing: content-box;
	color: #CC253D;
	display: inline-block;
	font-size: 1.42857rem;
	height: 1.42857rem;
	padding: 10px;
	width: 1.42857rem;
	-moz-transition: color 0.2s linear;
	-o-transition: color 0.2s linear;
	-webkit-transition: color 0.2s linear;
	transition: color 0.2s linear

}

.square-icon {
  background: none;
  text-align: center;
	border: 1px solid #A93B3F;
	border-radius: 10%;
	box-sizing: content-box;
	color: #A93B3F;
	display: inline-block;
	font-size: 1rem;
	height: 1rem;
	padding: 2px;
	width: 1rem;
	-moz-transition: color 0.2s linear;
	-o-transition: color 0.2s linear;
	-webkit-transition: color 0.2s linear;
	transition: color 0.2s linear

}
</style>

