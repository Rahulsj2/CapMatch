<template>
    <div class="container text-left">
        <router-link to="/profiles"><i class="icon fa fa-arrow-left mr-4"></i>Faculty Profiles</router-link>
        <div class="row mt-4 pt-4">
            <div class="col-lg-2">
                <span href="#" class="btn circle-iconn"></span>
            </div>
            <div class="col-lg-8 text-left">
                <h1 class="card-title">David Ebo Adjepon-Yamoah</h1>
                <p class="card-text">Computer Science and Information Systems</p>
                <div class="btn btn-green">
                    <span>Suggested</span>
                </div>
            </div>
            <div class="col-lg-2 d-flex justify-content-end">
                <i class="icon fa fa-plus square-icon"></i>
            </div>
        </div>
    </div>
</template>

<style>
.text-left > a{
    font-size: 16px;
    font-weight: 700;
    color: #A93B3F;
}
.btn-wine{
    background: #A93B3F;
    color: #fff;
    font-size: 16px;
    font-weight: 400;
}
.card-title{
    color: #707070;
}

.btn-wine:hover{
    color: #fff;
}
.card-text {
    font-size: 16px;
    font-weight: 400;
}
.card-title{
    font-size: 32px;
    font-weight: 700;
}

.btn-green{
 color: #39903F;
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

.circle-iconn {
  background: none;
  text-align: center;
	border: 3px solid #eee;
	border-radius: 50%;
	box-sizing: content-box;
	color: #CC253D;
	display: inline-block;
	font-size: 7.42857rem;
	height: 7.42857rem;
	padding: 10px;
	width: 7.42857rem;
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
	font-size: 2rem;
	height: 2rem;
	padding: 10px;
	width: 2rem;
	-moz-transition: color 0.2s linear;
	-o-transition: color 0.2s linear;
	-webkit-transition: color 0.2s linear;
	transition: color 0.2s linear

}
</style>

