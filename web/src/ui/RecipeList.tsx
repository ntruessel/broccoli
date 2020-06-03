import React from "react";
import { AppBar, createStyles, IconButton, Theme, Toolbar, Typography } from "@material-ui/core";
import { Menu } from "@material-ui/icons";
import { makeStyles } from "@material-ui/core/styles";

const useStyles = makeStyles((theme: Theme) =>
    createStyles({
        menuButton: {
            marginRight: theme.spacing(2)
        }
    })
);

export const RecipeList: React.FC = props => {
    const classes = useStyles();
    return <>
        <AppBar>
            <Toolbar>
                <IconButton edge="start" className={classes.menuButton} color="inherit" aria-label="menu">
                    <Menu/>
                </IconButton>
                <Typography variant="h6">Rezepte</Typography>
            </Toolbar>
        </AppBar>
    </>;
}
