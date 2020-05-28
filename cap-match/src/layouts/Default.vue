<template>

  <div class="wrapper">
    <nav id="sidebar">
      <div class="sidebar-header">
       <img src="../assets/ashesi-logo-black.png" width="100">
      </div>

      <ul class="list-unstyled components">
        <li class="active">
          <router-link to="/"><i class="fas fa-th-large"></i><br>Dashboard</router-link>
        </li>
        <li class="">
          <router-link to="/profiles"><i class="fas fa-chalkboard-teacher"></i><br>Faculty profiles</router-link>
        </li>
        <li class="">
          <router-link to="/choices"><i class="fas fa-heart"></i><br>My top Choices</router-link>
        </li>
        <li class="">
          <router-link to="/match"><i class="fas fa-user-friends"></i><br>Supervisor match</router-link>
        </li>
      </ul>
    </nav>

    <div id="content">

      <nav class="navbar navbar-expand-lg navbar-light">
        <div class="container-fluid">
           <div id="sidebarCollapse" class="btn btn-grey">
             <!-- <i class="fas fa-align-left"></i> -->
             <span>Student</span>
           </div>
           <div class="col-lg-1">
                <span href="#" class="btn circle-icon"></span>
            </div>
        </div>
      </nav>

      <br><br>
      <slot/>
    </div>
  </div>

</template>

<style>

a,
a:hover,
a:focus{
  color: #A3A3A3;
  text-decoration: none;
  transition: all 0.3s;
}

.wrapper{
  display: flex;
  text-decoration: none;
  transition: all 0.3s;
}

#sidebar{
  min-width:180px ;
  max-width: 180px;
  background: #FAFAFA;
  color: #A3A3A3;
  transition: all 0.3s;
  min-height: 100vh;
  max-height: 100vh;
}

#sidebar .active{
  margin-left: -100;
}

#sidebar .sidebar-header{
  padding: 20px;
}

#sidebar ul.components{
  margin-top: 40px;
  padding: 20px 0;
}

#sidebar ul li a{
  padding: 20px;
  font-size: 10;
  display: block;
}

#sidebar ul li a:hover{
  color: #A93B3F;
}
.btn-grey{
  margin-left: 60%;
  background: #DEDDDD;
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
#content{
  width: 100%;
  padding: 20px;
  max-height: 100vh;
}
</style>