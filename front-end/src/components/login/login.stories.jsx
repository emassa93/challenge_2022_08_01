import React from 'react'
import LogIn from "./login"

export default {
    title: "Login Page",
    component: LogIn,
}

const Template = (args) => {
    return <LogIn {...args} />;
};

export const LogInPage = Template.bind({});

LogInPage.args = {
};
