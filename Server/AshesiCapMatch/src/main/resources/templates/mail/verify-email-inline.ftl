<div style="background: #FAFAFA;padding: 20px;">

    <div>
        <img src="https://drive.google.com/uc?export=view&id=12XBWGwpHIJqlF2JaQlYm14PkzKb-7ZAs" width="100" height="24" style="max-width: 100%;display: block;margin-left: auto;margin-right: auto;color: #707070;">
    </div>

    <div style="max-width: 600px;background: #FFFFFF;font-family: 'Lato', sans-serif;color: #707070;margin: auto;padding-bottom: 10px;">
        <h2 style="text-align: center;padding: 32px 0px 56px;font-weight: 700;line-height: 29px;">Verify Your Email Address</h2>
        <img src="https://drive.google.com/uc?export=view&id=1XhvU5aqvWQqMtATLkptNk2UFFmy1g_ZH" width="201" height="173" style="max-width: 100%;display: block;margin-left: auto;margin-right: auto;color: #707070;">

        <h4 style="margin: 32px;font-size: 18px;font-weight: 500;line-height: 24px;">Hi ${firstname},</h4>
        
        <p style="margin: 32px;font-size: 16px;font-weight: 300;line-height: 24px;"> To activate your account, please click the button below to verify 
            your email address. Your account will not be created until your email
            address is confirmed.
        </p>

        <div>
            <button style="margin: 32px auto 0px;display: block;background-color: #A93B3F;border: none;padding: 13px 32px;cursor: pointer;border-radius: 5px;"><a class="button-text" href="${clientBasePath}/confirmAccount?confirmCode=${confirmCode}" style="color: #FFFFFF;font-family: 'Lato', sans-serif;font-size: 16px;text-align: center;text-decoration: none;">Verify Email</a></button>
        </div>

        <p style="margin: 32px;font-size: 16px;font-weight: 300;line-height: 24px; overflow-wrap: break-word; word-wrap: break-word;">Or verify using this link: <a href="${clientBasePath}/confirmAccount?confirmCode=${confirmCode}"  style="color: #707070;">${clientBasePath}/confirmAccount?confirmCode=${confirmCode}</a>
        </p>

        <p style="margin: 32px;font-size: 16px;font-weight: 300;line-height: 24px;">If you did not sign up to Ashesi CapMatch, please ignore this email or
            contact us at <a href="ashesi.edu.gh" style="color: #707070;">info@ashesi.edu.gh</a>
        </p>
    </div>

    <div style="background: none;text-align: center;">
        <p style="margin: 32px;font-size: 10px;font-weight: 300;line-height: 12px;font-family: 'Lato', sans-serif;color: #707070;">2020 Ashesi University, 1 University Avenue, Berekuso E/R, Ghana</p>
    </div>

</div>