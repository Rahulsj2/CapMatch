<template>
    <div class="signup py-2 text-left">
        <img src="../assets/ashesi-logo-search.png" width="100">
        <h4 class="header mb-3 pt-4">What SDGs are you interested in?</h4>
         <p class="desc">Pick all interests that apply</p>
        <div class="row  my-2 ">
            <div class="col-lg-3 col-sm-3 my-2 ">
                <div class="card" style="width: 6rem;">
                    <div class="card-body text-center">
                        <h5 class="card-title">1</h5>
                        <p class="card-text">No Poverty</p>
                    </div>
                </div>
            </div>
            <div class="col-lg-3 col-sm-3 my-1 ">
                <div class="card" style="width: 6rem;">
                    <div class="card-body text-center">
                        <h5 class="card-title">2</h5>
                        <p class="card-text">No Poverty</p>
                    </div>
                </div>
            </div>
           <div class="col-lg-3 col-sm-3 my-1 ">
                <div class="card" style="width: 6rem;">
                    <div class="card-body text-center">
                        <h5 class="card-title">3</h5>
                        <p class="card-text">No Poverty</p>
                    </div>
                </div>
            </div>

            <div class="col-lg-3 col-sm-3 my-1 ">
                <div class="card" style="width: 6rem;">
                    <div class="card-body text-center">
                        <h5 class="card-title">4</h5>
                        <p class="card-text">No Poverty</p>
                    </div>
                </div>
            </div>

           <div class="col-lg-3 my-1 ">
                <div class="card" style="width: 6rem;">
                    <div class="card-body text-center">
                        <h5 class="card-title">5</h5>
                        <p class="card-text">No Poverty</p>
                    </div>
                </div>
            </div>
            <div class="col-lg-3 my-1 ">
                <div class="card" style="width: 6rem;">
                    <div class="card-body text-center">
                        <h5 class="card-title">6</h5>
                        <p class="card-text">No Poverty</p>
                    </div>
                </div>
            </div>
           <div class="col-lg-3 my-1 ">
                <div class="card" style="width: 6rem;">
                    <div class="card-body text-center">
                        <h5 class="card-title">7</h5>
                        <p class="card-text">No Poverty</p>
                    </div>
                </div>
            </div>

            <div class="col-lg-3 my-1 ">
                <div class="card" style="width: 6rem;">
                    <div class="card-body text-center">
                        <h5 class="card-title">8</h5>
                        <p class="card-text">No Poverty</p>
                    </div>
                </div>
            </div>
            <div class="col-lg-3 my-1 ">
                <div class="card" style="width: 6rem;">
                    <div class="card-body text-center">
                        <h5 class="card-title">9</h5>
                        <p class="card-text">No Poverty</p>
                    </div>
                </div>
            </div>
            <div class="col-lg-3 my-1 ">
                <div class="card" style="width: 6rem;">
                    <div class="card-body text-center">
                        <h5 class="card-title">10</h5>
                        <p class="card-text">No Poverty</p>
                    </div>
                </div>
            </div>
           <div class="col-lg-3 my-1 ">
                <div class="card" style="width: 6rem;">
                    <div class="card-body text-center">
                        <h5 class="card-title">11</h5>
                        <p class="card-text">No Poverty</p>
                    </div>
                </div>
            </div> 
            <div class="col-lg-3 my-1 ">
                <div class="card" style="width: 6rem;">
                    <div class="card-body text-center">
                        <h5 class="card-title">12</h5>
                        <p class="card-text">No Poverty</p>
                    </div>
                </div>
            </div>

             <div class="col-lg-3 my-1 ">
                <div class="card" style="width: 6rem;">
                    <div class="card-body text-center">
                        <h5 class="card-title">13</h5>
                        <p class="card-text">No Poverty</p>
                    </div>
                </div>
            </div>
             <div class="col-lg-3 my-1 ">
                <div class="card" style="width: 6rem;">
                    <div class="card-body text-center">
                        <h5 class="card-title">14</h5>
                        <p class="card-text">No Poverty</p>
                    </div>
                </div>
            </div>

             <div class="col-lg-3 my-1 ">
                <div class="card" style="width: 6rem;">
                    <div class="card-body text-center">
                        <h5 class="card-title">15</h5>
                        <p class="card-text">No Poverty</p>
                    </div>
                </div>
            </div>
             <div class="col-lg-3 my-1 ">
                <div class="card" style="width: 6rem;">
                    <div class="card-body text-center">
                        <h5 class="card-title">16</h5>
                        <p class="card-text">No Poverty</p>
                    </div>
                </div>
            </div>
            <div class="col-lg-3 my-1 ">
                <div class="card" style="width: 6rem;">
                    <div class="card-body text-center">
                        <h5 class="card-title">17</h5>
                        <p class="card-text">No Poverty</p>
                    </div>
                </div>
            </div>

        </div>
        <div class="col-12 form-group py-4">
            <button class="btn btn-wine btn-lg col-12">Continue<i class="arrow fa fa-arrow-right mx-3"></i></button>
        </div>
       


        <div class="onboard-footer text-center pt-5">
            <p class="">Don&#39;t have an account? <a class="signup" href="#">Sign up</a></p>
        </div>
    </div>
</template>

<script>
// import { required, email, minLength } from 'vuelidate/lib/validators'
export default {
    name: "LoginForm",
    data: function(){
        return {
            email: '',
            password: ''
        }
    }

}
</script>

<style>
.card-title{
    font-size: 24px;
    font-weight: 700;
}
.card-text{
    font-size: 10px;
    font-weight: 400;
}
h4.header{
    font-size: 30px;
    font-weight: 200;
}


p.desc{
    font-size: 16px;
    font-weight: 200;
}

.btn-select{
    /* background: #A93B3F; */
    color: #707070;
    font-size: 16px;
    font-weight: 400;
    border-color: #DEDDDD;
    padding: 10px 20px;
    border-radius: 25px;
}

.btn-select:hover{
    color: #A93B3F;
    border-color: #A93B3F;
    background-color: rgba(169, 59, 63, 0.10);
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