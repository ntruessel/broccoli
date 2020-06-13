import React from 'react';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import { RecipeList } from './RecipeList';
import 'typeface-roboto';
import { CssBaseline } from '@material-ui/core';

export const App: React.FC = () => (
    <>
        <CssBaseline/>
        <Router>
            <Switch>
                <Route path="/">
                    <RecipeList/>
                </Route>
            </Switch>
        </Router>
    </>
);
