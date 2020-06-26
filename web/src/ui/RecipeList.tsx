import React from 'react';
import {
    AppBar,
    createStyles,
    IconButton,
    Theme,
    Toolbar,
    Typography,
    List,
    ListItem,
    ListItemText,
} from '@material-ui/core';
import { Menu, Search } from '@material-ui/icons';
import { makeStyles } from '@material-ui/core/styles';

const useStyles = makeStyles((theme: Theme) =>
    createStyles({
        menuButton: {
            marginRight: theme.spacing(2),
        },
        title: {
            flexGrow: 1,
        },
    }),
);

export const RecipeList: React.FC = () => {
    const classes = useStyles();
    const recipes = ['Spaghetti Carbonara', 'Broccoli'];

    return <>
        <AppBar>
            <Toolbar>
                <IconButton edge="start" className={classes.menuButton} color="inherit" aria-label="menu">
                    <Menu/>
                </IconButton>
                <Typography className={classes.title} variant="h6">Rezepte</Typography>
                <IconButton aria-label="search" color="inherit">
                    <Search/>
                </IconButton>
            </Toolbar>
        </AppBar>
        <Toolbar/>
        <List>
            {recipes.map(recipe => <ListItem button><ListItemText primary={recipe}/></ListItem>)}
        </List>
    </>;
};
