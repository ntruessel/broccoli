import React from 'react';
import { BrowserRouter as Router } from 'react-router-dom';
import { AppBar, Container, Toolbar } from '@material-ui/core';

function App() {
    return <>
        <Router>
            <AppBar position="fixed">
                <Toolbar/>
            </AppBar>
            <Container>
            </Container>
        </Router>
    </>;
}

export default App;
