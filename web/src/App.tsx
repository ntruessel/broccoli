import React from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import { RecipeOverview } from './pages/RecipeOverview';
import { CreateRecipe } from './pages/CreateRecipe';
import { routes } from './utils/routes';

function App() {
    return <>
        <Router>
            <Switch>
                <Route path={routes.overview} exact>
                    <RecipeOverview/>
                </Route>
                <Route path={routes.newRecipe}>
                    <CreateRecipe/>
                </Route>
            </Switch>
        </Router>
    </>;
}

export default App;
