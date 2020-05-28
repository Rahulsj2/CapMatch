import Vue from 'vue';

export default{
    state: {
        userSignup: {
            group: "",
            major:"",
            department:"",   
        },
        user: null,
        token: "",

    },
    getters: {
        getUserGroup(state){
            return state.userSignup.group;
        },
        getMajor(state){
            return state.userSignup.major;
        },
        getDepartment(state){
            return state.userSignup.department;
        },
        getUser (state){
            return state.user;
        },
        getToken (state){
            return state.user;
        }
    },
    mutations: {
        changeUserGroup (state,payload){
            state.userSignup.group = payload;
        },
        changeMajor (state,payload){
            state.userSignup.major = payload;
        },
        changeDepartment (state,payload){
            state.userSignup.department = payload;
        },
        changeUser (state,payload){
            state.user = payload;
        },
        changeToken (state,payload){
            state.token = payload;
        }
    },
    actions:{
        AuthToken: ({commit},response) => {
            commit("changeUser", response.data);
            const token = response.headers.authorization;
            // console.log(token)
            Vue.prototype.$http.post('/login/startsession',"", {headers:{'Authorization': token}}).then(res=>{
                if (res.status === 200){
                    Vue.prototype.$http.defaults.headers.common['x_auth_token'] = res.headers.x_auth_token
                }
            })
        },
        LOGIN: ( {dispatch}, payload) => {
            return Vue.prototype.$http.post('/login', payload).then((response) => {
                if (response.status === 200){
                    // console.log(response.data._links.addInterests.href);
                    dispatch("AuthToken",response);   
                }
                return Promise.resolve(response)
            })
        },
        REGISTER: ( {state,dispatch}, {firstname,lastname,email,password}) =>{
            if (state.userSignup.group === "student"){
                const major = state.userSignup.major;
                return Vue.prototype.$http.post('/signup/student', {
                    firstname,lastname,email,password,major
                })
                .then( (response) => {
                    if (response.status === 201){
                        dispatch("AuthToken",response);
                    }
                    return Promise.resolve(response)
                })
            }
            else if (state.userSignup.group === "faculty"){
                const department = state.userSignup.department;
                return Vue.prototype.$http.post('/signup/faculty', {
                    firstname,lastname,email,password,department
                } )
                .then( (response) => {
                    if (response.status === 201){
                        dispatch("AuthToken",response);
                    }
                    return Promise.resolve(response)
                })
            }
        },
        SENDINTERESTS: ( {state}, payload) => {  
            console.log(payload);   
            return Vue.prototype.$http.post(state.user._links.addInterests.href, payload)
            .then((response) =>{
                if (response.status === 200){
                    console.log('1111')
                }
                return Promise.resolve(response)
            })
        },
        SENDSDGS: ( {state}, payload) => {     
            return Vue.prototype.$http.post(state.user._links.setSDGs.href, payload)
            .then((response) =>{
                if (response.status === 200){
                    console.log("2222")
                }
                return Promise.resolve(response)
            })
        },
        CONFIRMUSER: ({commit},params) => {
            console.log(commit)
            return Vue.prototype.$http.post("/signup/confirm","",{params: {'confirmCode':params.confirmCode}})
            .then( response => {
                if (response.status === 200){
                    console.log('222')
                }
                return Promise.resolve(response)
            })
        }

    }
}