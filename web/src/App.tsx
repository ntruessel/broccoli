import React from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import { AppBar, Container, Toolbar } from '@material-ui/core';
import { RecipeOverview } from './pages/RecipeOverview';
import { CreateRecipe } from './pages/CreateRecipe';
import { routes } from './utils/routes';

function App() {
    return <>
        <Router>
            <AppBar position="fixed">
                <Toolbar/>
            </AppBar>
            <Container>
                <Switch>
                    <Route path={routes.overview} exact>
                        <RecipeOverview/>
                    </Route>
                    <Route path={routes.newRecipe}>
                        <CreateRecipe/>
                    </Route>
                </Switch>
            </Container>
        </Router>
    </>;
}

export default App;
