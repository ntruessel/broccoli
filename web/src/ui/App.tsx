import React from "react";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import { RecipeList } from "./RecipeList";
import 'typeface-roboto';

export const App: React.FC = () => (
    <Router>
        <Switch>
            <Route path="/">
                <RecipeList/>
            </Route>
        </Switch>
    </Router>
)