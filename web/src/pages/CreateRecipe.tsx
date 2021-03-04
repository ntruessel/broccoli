import { TextField, Typography } from '@material-ui/core';
import React from 'react';

export function CreateRecipe() {
    return <>
        <Typography variant="h4">Neues Rezept erfassen</Typography>
        <form>
            <TextField name="name" label="Name"/>
        </form>
    </>;
}
