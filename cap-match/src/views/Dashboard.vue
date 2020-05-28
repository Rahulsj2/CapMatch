<template>
    <div class="container">
        <h2>Hello Someone</h2>
        <p>
            Get Started on your capstone journey
        </p>
        <div class="row mt-4 pt-5">
            <div class="col-sm-6">
                <div class="card">
                <div class="card-body">
                    <img class="my-3" src="../assets/gift.svg" width="300">
                    <p class="card-text my-3">Look like you Top Choices <br> list is emapty</p>
                    <router-link to="/profiles" class="btn btn-wine my-4">Find Faculty</router-link>
                </div>
                </div>
            </div>
            <div class="col-sm-6">
                <!-- <div class="card">
                <div class="card-body"> -->
                    <img class="my-" src="../assets/calendar-dashboard.png" width="535">
                <!-- </div>
                </div> -->
            </div>
        </div>
    </div>
</template>

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

