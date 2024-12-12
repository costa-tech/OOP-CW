import React from 'react';

function ControlPanel({ isRunning, onStart, onStop, onReset }) {
  return React.createElement('div', { className: 'control-panel' },
    React.createElement('h2', null, 'System Controls'),
    React.createElement('div', { className: 'system-status' },
      React.createElement('div', { 
        className: `status-indicator ${isRunning ? 'running' : 'stopped'}` 
      }, `System is ${isRunning ? 'Running' : 'Stopped'}`)
    ),
    React.createElement('div', { className: 'button-group' },
      React.createElement('button', {
        className: 'control-btn start',
        onClick: onStart,
        disabled: isRunning
      }, 'Start System'),
      React.createElement('button', {
        className: 'control-btn stop',
        onClick: onStop,
        disabled: !isRunning
      }, 'Stop System'),
      React.createElement('button', {
        className: 'control-btn reset',
        onClick: onReset,
        disabled: isRunning
      }, 'Reset System')
    )
  );
}

export default ControlPanel;
