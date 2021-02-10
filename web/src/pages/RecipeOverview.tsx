import React from 'react';
import { Fab as MuiFab, styled } from '@material-ui/core';
import { Add } from '@material-ui/icons';
import { useHistory } from 'react-router-dom';
import { routes } from '../utils/routes';

export function RecipeOverview() {
    const history = useHistory();
    return <>
        <Fab color="primary" onClick={() => {
            history.push(routes.newRecipe)
        }}>
            <Add/>
        </Fab>
    </>;
}

const Fab = styled(MuiFab)(({ theme }) => ({
    position: 'absolute',
    bottom: theme.spacing(2),
    right: theme.spacing(2),
}));
