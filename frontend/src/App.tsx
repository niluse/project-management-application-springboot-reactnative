import React from 'react';
import { StatusBar, SafeAreaView } from 'react-native';
import { Provider as PaperProvider, DefaultTheme } from 'react-native-paper';
import AppNavigator from './navigation/AppNavigator';

// Define a custom theme for React Native Paper
const theme = {
  ...DefaultTheme,
  colors: {
    ...DefaultTheme.colors,
    primary: '#2196F3',
    accent: '#03A9F4',
  },
};

const App = () => {
  return (
    <PaperProvider theme={theme}>
      <SafeAreaView style={{ flex: 1 }}>
        <StatusBar barStyle="dark-content" backgroundColor="#fff" />
        <AppNavigator />
      </SafeAreaView>
    </PaperProvider>
  );
};

export default App; 